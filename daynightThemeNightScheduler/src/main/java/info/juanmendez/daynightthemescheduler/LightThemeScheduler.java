package info.juanmendez.daynightthemescheduler;

import info.juanmendez.daynightthemescheduler.models.LightThemeClientResponse;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightThemePlanner;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;

/**
 * Created by Juan Mendez on 10/17/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

class LightThemeScheduler {

    LightThemeClient client;
    LightThemePlanner planner;

    public LightThemeScheduler(LightThemeClient lightThemeClient) {
        client = lightThemeClient;
        planner = new LightThemePlanner( client.getLightTimeModule(), client.getAppLightTime() );
    }

    public void provideNextSchedule( Response<LightThemeClientResponse> response ){
        if( client.getLightTimeModule().getObserversCount() == 0 ){
            response.onResult( LightThemeClientResponse.create(LightTimeStatus.CANCEL_ANY_SCHEDULE));
        }else{
            planner.provideNextTimeLight( lightTimeResult -> {
                if(LightTimeUtils.isValid(lightTimeResult )){
                    response.onResult( LightThemeClientResponse.create( LightTimeStatus.NEXT_SCHEDULE, lightTimeResult ));
                }else{
                    response.onResult( LightThemeClientResponse.create( lightTimeResult.getStatus(), lightTimeResult ) );
                }
            });
        }
    }
}
