package info.juanmendez.daynightthemescheduler.utils;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightTimeUtils {
    public static LightTime clone(LightTime that ){
        LightTime clone = new LightTime( that.getSunrise(), that.getSunset() );
        clone.setNextSchedule( that.getNextSchedule() );
        return clone;
    }

    public static LightTime clonedAsGuessed(LightTime appLighttime, int daysFromToday) {
        LightTime clone = new LightTime();
        clone.setSunrise( LocalTimeUtils.getDayAsString( appLighttime.getSunrise(), daysFromToday ));
        clone.setSunset( LocalTimeUtils.getDayAsString( appLighttime.getSunset(), daysFromToday ));
        clone.setStatus(LightTimeStatus.LIGHTTIME_GUESSED);
        return clone;
    }

    public static boolean isValid( LightTime that ){
        return !that.getSunrise().isEmpty() && !that.getSunset().isEmpty();
    }
}
