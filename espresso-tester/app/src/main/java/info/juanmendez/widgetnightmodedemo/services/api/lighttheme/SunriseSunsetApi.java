package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightApi;


/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class SunriseSunsetApi implements LightApi {

   private static LightTime sLightTimeToday = new LightTime();



    private static LightTime sLightTimeTomorrow = new LightTime();


    @Override
    public void generateTodayTimeLight(Response<LightTime> response) {
        response.onResult( sLightTimeToday );
    }

    @Override
    public void generateTomorrowTimeLight(Response<LightTime> response) {
        response.onResult( sLightTimeTomorrow );
    }

    public static LightTime getLightTimeToday() {
        return sLightTimeToday;
    }

    public static void setLightTimeToday(LightTime lightTimeToday) {
        sLightTimeToday = lightTimeToday;
    }

    public static LightTime getLightTimeTomorrow() {
        return sLightTimeTomorrow;
    }

    public static void setLightTimeTomorrow(LightTime lightTimeTomorrow) {
        sLightTimeTomorrow = lightTimeTomorrow;
    }
}
