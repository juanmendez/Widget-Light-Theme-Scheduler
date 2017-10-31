package info.juanmendez.daynightthemescheduler;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import info.juanmendez.daynightthemescheduler.services.LightPlanner;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import timber.log.Timber;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeClient {
    private static final String CLASS_NAME = LightThemeClient.class.getName();
    public static final String SCHEDULE_COMPLETED = CLASS_NAME + ".SCHEDULE_COMPLETED";
    public static final String AUTO_TURNED_ON = CLASS_NAME + ".AUTO_TURNED_ON";
    public static final String AUTO_TURNED_OFF = CLASS_NAME + ".AUTO_TURNED_OFF";

    private LightThemeModule m;
    private LightWidgetService widgetService;
    private LightAlarmService alarmService;
    private LightPlanner planner;


    public LightThemeClient(LightThemeModule module, LightWidgetService widgetService, LightAlarmService alarmService, LightTimeStorage lightTimeStorage) {
        this.m = module;
        this.widgetService = widgetService;
        this.alarmService = alarmService;
        planner = new LightPlanner(m);
    }

    /**
     * widget added/removed. device reboot
     */
    public void onClientEvent( @NonNull String actionEvent ){

        if(actionEvent.equals(AUTO_TURNED_ON)){
            Timber.i( "theme night auto on");
        }else if( actionEvent.equals( AUTO_TURNED_OFF)){
            Timber.i( "theme night auto off");
        }else if( actionEvent.equals( AppWidgetManager.ACTION_APPWIDGET_ENABLED)){
            Timber.i( "widget added");
        }else if( actionEvent.equals( AppWidgetManager.ACTION_APPWIDGET_DELETED)){
            Timber.i( "widget removed");
        }else if( actionEvent.equals(Intent.ACTION_REBOOT)){
            Timber.i( "device rebooted");
        }else if( actionEvent.equals(SCHEDULE_COMPLETED)){
            Timber.i( "schedule completed");
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

    public void  planNextSchedule(){

        if( widgetService.getObserversCount() > 0 ){

            planner.provideNextTimeLight( lightTimeResult -> {

                if(LightTimeUtils.isValid(lightTimeResult )){
                    alarmService.scheduleNext( lightTimeResult );
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
}