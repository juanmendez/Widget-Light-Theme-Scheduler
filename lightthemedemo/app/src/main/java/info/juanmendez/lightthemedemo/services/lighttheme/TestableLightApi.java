package info.juanmendez.lightthemedemo.services.lighttheme;

import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.Response;
import info.juanmendez.lightthemescheduler.services.LightApi;


/**
 * Created by Juan Mendez on 11/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * When testing lets use static values.
 */
public class TestableLightApi implements LightApi {
    @Override
    public void generateTodayTimeLight(Response<LightTime> response) {
        LightTime todayLightTime = new LightTime("2017-11-07T12:31:26+00:00", "2017-11-07T17:55:00+00:00");
        response.onResult( todayLightTime );
    }

    @Override
    public void generateTomorrowTimeLight(Response<LightTime> response) {
        LightTime tomorrowLightTime = new LightTime("2017-11-07T18:10:39+00:00","2017-11-08T22:37:02+00:00");
        response.onResult( tomorrowLightTime );
    }
}
