package info.juanmendez.daynightthemescheduler;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import info.juanmendez.daynightthemescheduler.services.LightPlanner;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeClient {
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

    /**
     * Schedule finished its task, and lets the theme know.
     */
    public void onScheduleComplete(){

    }

    /**
     * There hasn't been any schedule done.
     * This is the case of device reboot, or first time adding a widget
     */
    public void onScheduleRequest(){
        //We want to enforce lightTime in module has no schedule
        m.getLightTime().setNextSchedule("");

    }

}