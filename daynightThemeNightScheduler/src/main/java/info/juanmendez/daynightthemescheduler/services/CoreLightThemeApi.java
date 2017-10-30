package info.juanmendez.daynightthemescheduler.services;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils;

/**
 * Created by Juan Mendez on 10/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class CoreLightThemeApi implements LightThemeApi {
    LightThemeModule m;

    public CoreLightThemeApi(LightThemeModule module  ) {
        m = module;
    }

    /**
     * We attempt to collect data already cached in.
     * Otherwise we try to make a webservice call to get the data.
     * In the most optimistic case having no network we clone data from another date into todays.
     * In the worst case we reply with an empty LightTime, which is considered invalid to our app.
     * @param response
     */
    @Override
    public void generateTodayTimeLight(Response<LightTime> response) {
        //we check if what we have is already cached
        LightTime appLightTime = m.getLightTime();

        if(LocalTimeUtils.isSameDay( appLightTime.getSunrise(), m.getNow().toDateTimeToday().toString() )){
            response.onResult( LightTimeUtils.clone(appLightTime) );
        }else if( m.getNetworkService().isOnline() && m.getLocationService().isGranted() ){
             m.getLightTimeApi().generateTodayTimeLight( response );
        }else if( LightTimeUtils.isValid(appLightTime)){
            response.onResult( LightTimeUtils.clonedAsGuessed(appLightTime, 0 ) );
        }else{
            response.onResult( getErrorLightTime() );
        }
    }

    /**
     * We attempt to collect data from a webservice call to get the tomorrow's data.
     * In the most optimistic case having no network we clone data from another date into tomorrows.
     * In the worst case we reply with an empty LightTime, which is considered invalid to our app.
     * @param response
     */
    @Override
    public void generateTomorrowTimeLight(Response<LightTime> response) {
        LightTime appLightTime = m.getLightTime();

        if( m.getNetworkService().isOnline() && m.getLocationService().isGranted() ){
            m.getLightTimeApi().generateTomorrowTimeLight( response );
        }else if( LightTimeUtils.isValid(appLightTime)){
            response.onResult( LightTimeUtils.clonedAsGuessed(appLightTime, 1 ) );
        }else{
            response.onResult( getErrorLightTime() );
        }
    }

    private LightTime getErrorLightTime(){
        LightTime lightTime = new LightTime();

        if( !m.getNetworkService().isOnline()  )
            lightTime.setStatus(LightTimeStatus.NO_INTERNET);
        else if( !m.getLocationService().isGranted()  )
            lightTime.setStatus(LightTimeStatus.NO_LOCATION_PERMISSION );

        return lightTime;
    }
}
