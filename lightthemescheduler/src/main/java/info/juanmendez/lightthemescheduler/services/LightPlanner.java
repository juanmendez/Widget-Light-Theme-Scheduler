package info.juanmendez.lightthemescheduler.services;

import org.joda.time.LocalTime;

import info.juanmendez.lightthemescheduler.models.LightThemeModule;
import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.Response;
import info.juanmendez.lightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.lightthemescheduler.utils.LocalTimeUtils;

/**
 * Created by Juan Mendez on 10/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This class provides a LightTime having the next schedule.
 * If a LightTime points to next day, then it caches the LightTime for current day.
 */
public class LightPlanner {

    public static final int NO_SCHEDULE = 0;
    public static final int SUNRISE_SCHEDULE = 1;
    public static final int SUNSET_SCHEDULE = 2;
    public static final int TOMORROW_SCHEDULE = 3;

    private CoreLightApi mApiProxy;
    private LightThemeModule m;
    private LightTime mTodayLightTime = new LightTime();

    /**
     * @param module
     */
    public LightPlanner(LightThemeModule module ) {
        mApiProxy = new CoreLightApi( module );
        m = module;
    }

    public void generateLightTime(Response<LightTime> response){
        generateTodayLightTime(  response );
    }

    private void generateTodayLightTime(Response<LightTime> response ){
        mApiProxy.generateTodayTimeLight(todayLightTime -> {

            //try to fix in case not valid.
            if(!LightTimeUtils.isValid( todayLightTime ) && LightTimeUtils.isValid(m.getLightTime())){
                todayLightTime = LightTimeUtils.clonedAsGuessed( m.getLightTime(), 0 );
            }

            if(LightTimeUtils.isValid( todayLightTime )){
                mTodayLightTime = LightTimeUtils.clone( todayLightTime );

                LocalTime sunrise = LocalTimeUtils.getLocalTime( todayLightTime.getSunrise() );
                LocalTime sunset = LocalTimeUtils.getLocalTime( todayLightTime.getSunset() );

                int when = whatSchedule( m.getNow() , sunrise, sunset );

                if( when == SUNRISE_SCHEDULE ){
                    todayLightTime.setNextSchedule( todayLightTime.getSunrise() );
                    response.onResult( todayLightTime );
                }else if(  when == SUNSET_SCHEDULE ){
                    todayLightTime.setNextSchedule( todayLightTime.getSunset() );
                    response.onResult( todayLightTime );
                }else if( when == TOMORROW_SCHEDULE ){
                    //ok, we need to call and get tomorrows..
                    generateTomorrowLightTime( response );
                }
            }else{
                response.onResult( todayLightTime );
            }
        });
    }

    private void generateTomorrowLightTime(Response<LightTime> response ){
        mApiProxy.generateTomorrowTimeLight(tomorrowTimeLight -> {

            //try to fix in case not valid.
            if(!LightTimeUtils.isValid( tomorrowTimeLight ) && LightTimeUtils.isValid(m.getLightTime())){
                tomorrowTimeLight = LightTimeUtils.clonedAsGuessed( m.getLightTime(), 1 );
            }

            if( LightTimeUtils.isValid( tomorrowTimeLight )){
                tomorrowTimeLight.setNextSchedule( tomorrowTimeLight.getSunrise() );
                response.onResult( tomorrowTimeLight );
            }else{
                response.onResult( tomorrowTimeLight );
            }
        });
    }

    public static int whatSchedule( LocalTime now, LocalTime sunrise, LocalTime sunset ){

        if( now.isBefore( sunrise )){
            return SUNRISE_SCHEDULE;
        }else if( now.isBefore( sunset) ){
            return SUNSET_SCHEDULE;
        }else{
            return TOMORROW_SCHEDULE;
        }
    }

    public LightTime getTodayLightTime(){
        return mTodayLightTime;
    }
}