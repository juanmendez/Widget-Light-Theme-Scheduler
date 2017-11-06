package info.juanmendez.lightthemescheduler.services;

import info.juanmendez.lightthemescheduler.models.LightThemeModule;
import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.LightTimeStatus;
import info.juanmendez.lightthemescheduler.models.Response;
import info.juanmendez.lightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.lightthemescheduler.utils.LocalTimeUtils;

/**
 * Created by Juan Mendez on 10/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This class wraps the LightApi defined in the application. It is access directly by this library.
 * In this way, a lot of the logic sits here. And the one in the application generates the LightTime object.
 */
public class CoreLightApi implements LightApi {
    LightThemeModule m;

    public CoreLightApi(LightThemeModule module  ) {
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
        boolean isSameDay = LocalTimeUtils.isSameDay( appLightTime.getSunrise(), m.getNow().toDateTimeToday().toString() );

        if( isSameDay){
            response.onResult( LightTimeUtils.clone(appLightTime) );
        }else if( isCallAllowed() ){
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

        if( isCallAllowed() ){
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
        else if( !m.getLocationService().isGranted() )
            lightTime.setStatus(LightTimeStatus.NO_LOCATION_PERMISSION );
        else if(  m.getLocationService().getLocation() == null ){
            lightTime.setStatus( LightTimeStatus.NO_LOCATION_AVAILABLE );
        }

        return lightTime;
    }

    private boolean isCallAllowed(){
        return  m.getNetworkService().isOnline() &&
                m.getLocationService().isGranted() &&
                m.getLocationService().getLocation() != null;
    }
}
