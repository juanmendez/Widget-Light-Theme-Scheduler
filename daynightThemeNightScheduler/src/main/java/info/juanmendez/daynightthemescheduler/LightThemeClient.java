package info.juanmendez.daynightthemescheduler;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.services.LightThemeAlarmService;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeClient {
    private LightThemeModule m;
    private LightWidgetService widgetService;
    private LightThemeScheduler scheduler;
    private LightThemeAlarmService alarmService;


    public LightThemeClient( LightThemeModule module, LightWidgetService widgetService, LightThemeAlarmService alarmService) {
        this.m = module;
        this.widgetService = widgetService;
        this.alarmService = alarmService;
    }

    public void  planNextSchedule(){
        if( widgetService.getObserversCount() > 0 ){

        }else{

        }
    }

    public LightThemeModule getLightThemeModule() {
        return m;
    }
}