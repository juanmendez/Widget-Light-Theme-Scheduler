package info.juanmendez.daynightthemescheduler;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.WidgetScreenStatus;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import info.juanmendez.daynightthemescheduler.services.LightPlanner;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import timber.log.Timber;

import static info.juanmendez.daynightthemescheduler.utils.LightTimeUtils.getScreenMode;


/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * TODO: rename to LightThemeManager
 */
public class LightThemeClient {

    private static final String CLASS_NAME = LightThemeClient.class.getName();
    public static final String THEME_OPTION_CHANGED = CLASS_NAME + ".THEME_OPTION_CHANGED";
    public static final String SCHEDULE_COMPLETED = CLASS_NAME + ".SCHEDULE_COMPLETED";
    public static final String SCHEDULE_WHEN_ONLINE = CLASS_NAME + ".SCHEDULE_WHEN_ONLINE";


    private LightThemeModule m;
    private LightWidgetService widgetService;
    private LightAlarmService alarmService;
    private LightPlanner planner;


    public LightThemeClient(LightThemeModule module, LightWidgetService widgetService, LightAlarmService alarmService) {
        this.m = module;
        this.widgetService = widgetService;
        this.alarmService = alarmService;
        planner = new LightPlanner(m);
    }

    /**
     * widget added/removed. device reboot
     */
    public void onAppEvent(@NonNull String actionEvent ){

        if(actionEvent.equals(THEME_OPTION_CHANGED)){
            Timber.i( "theme option changed");

            if( widgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){
                planNextSchedule();
            }else{
                alarmService.cancelIfRunning();
            }
        }else if( actionEvent.equals( AppWidgetManager.ACTION_APPWIDGET_ENABLED)){
            planNextSchedule();
        }else if( actionEvent.equals( AppWidgetManager.ACTION_APPWIDGET_DELETED) && widgetService.getWidgetsCount() == 0 ){
            alarmService.cancelIfRunning();
        }else if( actionEvent.equals(Intent.ACTION_REBOOT)){
            planNextSchedule();
        }else if( actionEvent.equals(SCHEDULE_COMPLETED)){
            planNextSchedule();
        }else{
            //if it doesn't match any actions, then skip
            return;
        }

        reflectScreenMode();
    }

    /**
     * There hasn't been any schedule done.
     * This is the case of device reboot, or first time adding a widget
     * We will attempt to start
     */
    public void onScheduleRequest(){
        //We want to enforce lightTime in module has no schedule
        m.getLightTime().setNextSchedule("");
        planNextSchedule();
    }

    public void  planNextSchedule(){

        if( widgetService.getWidgetsCount() > 0 && widgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){

            planner.provideNextTimeLight( lightTimeResult -> {

                m.getLightTimeStorage().saveLightTime( lightTimeResult );

                if(LightTimeUtils.isValid(lightTimeResult )){
                    alarmService.scheduleNext( LightTimeUtils.getMSFromSchedule(lightTimeResult)  );
                }else if( lightTimeResult.getStatus() == LightTimeStatus.NO_INTERNET ){

                    lightTimeResult.setNextSchedule("");
                    alarmService.scheduleNextWhenOnline();
                }else{
                    alarmService.cancelIfRunning();
                }
            });
        }else{
            alarmService.cancelIfRunning();
        }
    }

    public void reflectScreenMode(){

        LightTime todayLightTime = planner.getTodayLightTime();

        if( widgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO && LightTimeUtils.isValid( todayLightTime ) ){
            widgetService.setWidgetScreenMode( getScreenMode( m.getNow(), todayLightTime ) );
        }else if( widgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_YES ){
            widgetService.setWidgetScreenMode( WidgetScreenStatus.WIDGET_NIGHT_SCREEN );
        }else if( widgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_NO ) {
            widgetService.setWidgetScreenMode( WidgetScreenStatus.WIDGET_DAY_SCREEN );
        }
    }
}