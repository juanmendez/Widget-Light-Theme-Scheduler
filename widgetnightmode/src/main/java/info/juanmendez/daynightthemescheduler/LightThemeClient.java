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
    public static final String ALARM_EXECUTED = CLASS_NAME + ".ALARM_EXECUTED";
    public static final String ALARM_EXECUTED_ONLINE = CLASS_NAME + ".ALARM_EXECUTED_ONLINE";

    private LightThemeModule m;
    private LightWidgetService mWidgetService;
    private LightAlarmService mAlarmService;
    private LightPlanner mPlanner;

    public LightThemeClient(LightThemeModule module, LightWidgetService widgetService, LightAlarmService alarmService) {
        m = module;
        mWidgetService = widgetService;
        mAlarmService = alarmService;
        mPlanner = new LightPlanner(m);
    }

    /**
     * widget added/removed. device reboot
     */
    public void onAppEvent(@NonNull String appEvent ){

        Timber.i( "widget action %s", appEvent );

        if(appEvent.equals(THEME_OPTION_CHANGED)){
            Timber.i( "theme option changed");

            if( mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){
                planNextSchedule();
            }else{
                reflectScreenMode();
                mAlarmService.cancelIfRunning();
            }
        }else if( appEvent.equals( AppWidgetManager.ACTION_APPWIDGET_ENABLED) ){
            if( mWidgetService.getWidgetsCount() == 1 ){
                planNextSchedule();
            }
        }else if( appEvent.equals( AppWidgetManager.ACTION_APPWIDGET_DELETED) ){
            if( mWidgetService.getWidgetsCount() == 0 ){
                mAlarmService.cancelIfRunning();
            }
        }else if( appEvent.equals(Intent.ACTION_REBOOT)){
            planNextSchedule();
        }else if( appEvent.equals(ALARM_EXECUTED)){
            planNextSchedule();
        }else if( appEvent.equals(ALARM_EXECUTED_ONLINE)){
            planNextSchedule();
        }
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

    private void  planNextSchedule(){
        Timber.i( "plan next schedule... " + mWidgetService.getWidgetsCount() );
        Timber.i( "in auto mode? " + ( mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO? "yes":"noe"));
        if( mWidgetService.getWidgetsCount() > 0 && mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){

            mPlanner.provideNextTimeLight(lightTimeResult -> {

                Timber.i( "result %s", lightTimeResult );
                m.getLightTimeStorage().saveLightTime( lightTimeResult );

                if(LightTimeUtils.isValid(lightTimeResult )){
                    mAlarmService.scheduleNext( LightTimeUtils.getMSFromSchedule(lightTimeResult)  );
                }else if( lightTimeResult.getStatus() == LightTimeStatus.NO_INTERNET ){

                    lightTimeResult.setNextSchedule("");
                    mAlarmService.scheduleNextWhenOnline();
                }else{
                    mAlarmService.cancelIfRunning();
                }

                reflectScreenMode();
            });
        }else{
            mAlarmService.cancelIfRunning();
            reflectScreenMode();
        }
    }

    public void reflectScreenMode(){

        LightTime todayLightTime = mPlanner.getTodayLightTime();
        int nextScreenMode = mWidgetService.getWidgetScreenMode();

        if( mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO && LightTimeUtils.isValid( todayLightTime ) ){
            nextScreenMode = getScreenMode( m.getNow(), todayLightTime );
        }else if( mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_YES ){
            nextScreenMode = WidgetScreenStatus.WIDGET_NIGHT_SCREEN;
        }else if( mWidgetService.getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_NO ) {
            nextScreenMode = WidgetScreenStatus.WIDGET_DAY_SCREEN;
        }

        if( nextScreenMode != mWidgetService.getWidgetScreenMode() ){
            mWidgetService.setWidgetScreenMode( nextScreenMode );
        }
    }
}