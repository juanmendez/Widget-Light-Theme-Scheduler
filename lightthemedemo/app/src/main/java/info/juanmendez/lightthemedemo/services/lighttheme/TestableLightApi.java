package info.juanmendez.lightthemedemo.services.lighttheme;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.Response;
import info.juanmendez.lightthemescheduler.services.LightApi;
import info.juanmendez.lightthemescheduler.utils.LocalTimeUtils;


/**
 * Created by Juan Mendez on 11/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This is a freeby. In case you want to test your app.
 *
 * Right now is nov 7, 12:16 US central time.. 6 hours behind UTC
 * Apparently sunrise is at 12:29 today. I know this is awkward but this is a demo showing how I get to test
 * the alarm by manipulating today's or tomorrow's times.
 */
public class TestableLightApi implements LightApi {
    LocalDateTime mDateTime;

    public TestableLightApi(String parcelableTime) {
        mDateTime = LocalTime.parse(parcelableTime).toDateTimeToday().toLocalDateTime();
    }

    @Override
    public void generateTodayTimeLight(Response<LightTime> response) {
        //sunrise 3 hours ago
        //sunset 10 minute from now
        LightTime lightTime = new LightTime( getUTC( mDateTime.minusHours(3) ) , getUTC(mDateTime.plusMinutes(10)) );
        response.onResult( lightTime );
    }

    @Override
    public void generateTomorrowTimeLight(Response<LightTime> response) {

        //sunrise 25 minutes from now.
        //sunset 45 minutes from now
        LightTime lightTime = new LightTime( getUTC( mDateTime.plusMinutes(25)) , getUTC(mDateTime.plusMinutes(45)) );
        response.onResult( lightTime );
    }

    private String getUTC( LocalDateTime localTime ){
        return LocalTimeUtils.getUTC( localTime.toDateTime() ).toString();
    }
}