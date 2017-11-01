package info.juanmendez.daynightthemescheduler.utils;

import android.support.annotation.NonNull;

import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.WidgetScreenStatus;

import static info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils.getLocalTime;
import static info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils.isDaylightScreen;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightTimeUtils {
    public static LightTime clone(LightTime that ){
        LightTime clone = new LightTime( that.getSunrise(), that.getSunset() );
        clone.setNextSchedule( that.getNextSchedule() );
        clone.setStatus( that.getStatus() );
        return clone;
    }

    public static LightTime clonedAsGuessed(LightTime appLighttime, int daysFromToday) {
        LightTime clone = new LightTime();
        copy( appLighttime, clone );
        clone.setSunrise( LocalTimeUtils.getDayAsString(clone.getSunrise(), daysFromToday));
        clone.setSunset( LocalTimeUtils.getDayAsString(clone.getSunset(), daysFromToday));
        clone.setStatus(LightTimeStatus.LIGHTTIME_GUESSED);
        return clone;
    }

    public static void copy(@NonNull  LightTime original, @NonNull  LightTime copied ){
        copied.setSunset( original.getSunset() );
        copied.setSunrise( original.getSunrise() );
        copied.setNextSchedule( original.getNextSchedule() );
        copied.setStatus( original.getStatus() );
    }

    public static void clear( @NonNull LightTime original ){
        original.setSunset( "" );
        original.setSunrise( "" );
        original.setNextSchedule( "" );
        original.setStatus( 0 );
    }

    public static boolean isValid( LightTime that ){
        return !that.getSunrise().isEmpty() && !that.getSunset().isEmpty();
    }

    public static int getScreenMode(LocalTime now, LightTime lightTime){
        if( isDaylightScreen( now, getLocalTime(lightTime.getSunrise()), getLocalTime(lightTime.getSunset()) ))
            return WidgetScreenStatus.WIDGET_DAY_SCREEN;
        else
            return WidgetScreenStatus.WIDGET_NIGHT_SCREEN;
    }
}
