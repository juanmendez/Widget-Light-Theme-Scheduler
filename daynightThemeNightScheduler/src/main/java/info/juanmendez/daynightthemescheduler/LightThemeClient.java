package info.juanmendez.daynightthemescheduler;

import info.juanmendez.daynightthemescheduler.models.LightThemeClientResponse;
import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import info.juanmendez.daynightthemescheduler.services.LightThemePlanner;
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
    private LightThemePlanner planner;


    public LightThemeClient( LightThemeModule module, LightWidgetService widgetService, LightAlarmService alarmService) {
        this.m = module;
        this.widgetService = widgetService;
        this.alarmService = alarmService;
        planner = new LightThemePlanner(m);
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

    public void provideNextSchedule( Response<LightThemeClientResponse> response ){
        planner.provideNextTimeLight( lightTimeResult -> {
            if(LightTimeUtils.isValid(lightTimeResult )){
                response.onResult( LightThemeClientResponse.create( LightTimeStatus.NEXT_SCHEDULE, lightTimeResult ));
            }else{
                response.onResult( LightThemeClientResponse.create( lightTimeResult.getStatus(), lightTimeResult ) );
            }
        });
    }

    public LightThemeModule getLightThemeModule() {
        return m;
    }
}