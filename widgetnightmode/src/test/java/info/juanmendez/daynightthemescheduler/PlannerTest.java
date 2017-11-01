package info.juanmendez.daynightthemescheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.CoreLightApi;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils;

import static info.juanmendez.daynightthemescheduler.services.LightPlanner.SUNRISE_SCHEDULE;
import static info.juanmendez.daynightthemescheduler.services.LightPlanner.SUNSET_SCHEDULE;
import static info.juanmendez.daynightthemescheduler.services.LightPlanner.TOMORROW_SCHEDULE;
import static info.juanmendez.daynightthemescheduler.services.LightPlanner.whatSchedule;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * These tests were made in order to make the functionality needed in CoreLightApi
 */
public class PlannerTest extends LightThemeTest{
    LightTime appLightTime;

    @Before
    public void before(){
        appLightTime = twistedStorage.getLightTime();
    }

    @Test
    public void firstTime(){

        //if values in appLightTime are empty, we need to get today's appLightTime
        if( appLightTime.getSunrise().isEmpty() || appLightTime.getSunrise().isEmpty() ){

            //we are defining what proxy is going to return next
            twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
            twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

            twistedApi.asMocked().generateTodayTimeLight(result -> {
                appLightTime.setSunrise( result.getSunrise());
                appLightTime.setSunset( result.getSunset());
            });
        }

        //do we schedule for twistSunrise, twistSunset, or none?
        LocalTime twistSunrise = LocalTimeUtils.getLocalTime( appLightTime.getSunrise() );
        LocalTime twistSunset = LocalTimeUtils.getLocalTime( appLightTime.getSunset() );

        assertEquals( whatSchedule(  LocalTime.parse( "00:40:00"), twistSunrise, twistSunset), SUNRISE_SCHEDULE  );
        assertEquals( whatSchedule(  LocalTime.parse( "16:40:00"), twistSunrise, twistSunset), SUNSET_SCHEDULE  );
        assertEquals( whatSchedule(  LocalTime.parse( "23:44:00"), twistSunrise, twistSunset), TOMORROW_SCHEDULE  );

        //what if we need tomorrows appLightTime instead?
        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:26+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:42+00:00" );

        twistedApi.asMocked().generateTomorrowTimeLight(result -> {
            appLightTime.setSunrise( result.getSunrise());
            appLightTime.setSunset( result.getSunset());
        });

        //so proxy is giving back twistSunrise for tomorrow.
        assertEquals( LocalTimeUtils.getLocalDateTime(appLightTime.getSunrise()).toLocalDate(), LocalDate.parse("2017-10-27").plusDays(1) );
    }

    @Test
    public void firstTimeInPlanner(){
        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:26+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:42+00:00" );

        m.applyNow( LocalTime.parse( "00:40:00" ) );
        planner.provideNextTimeLight(result -> {
            assertEquals(twistedApi.getToday().getSunrise(), result.getNextSchedule());
        });


        m.applyNow( LocalTime.parse( "16:40:00" ) );
        planner.provideNextTimeLight(result -> {
            assertEquals(twistedApi.getToday().getSunset(), result.getNextSchedule());
        });

        m.applyNow( LocalTime.parse( "23:00:00" ) );
        planner.provideNextTimeLight(result -> {
            assertEquals( result.getSunrise(), twistedApi.getTomorrow().getSunrise() );
            assertEquals( result.getSunset(), twistedApi.getTomorrow().getSunset() );
            assertEquals( result.getNextSchedule(), twistedApi.getTomorrow().getSunrise() );
        });
    }

    /**
     * this is the userOption which should be taken whenever there is no network connection.
     * we use what's available from the day before.
     */
    @Test
    public void offlineTest(){
        twistedNetwork.isOnline = false;

        String yesterdaySunrise = "2017-10-26T12:07:26+00:00";
        String yesterdaySunset = "2017-10-26T23:03:42+00:00";

        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( yesterdaySunrise );
        appLightTime.setSunset( yesterdaySunset );

        //reuse what's on appLightTime from another date into today
        twistedApi.getToday().setSunrise( LocalTimeUtils.getDayAsString( appLightTime.getSunrise(), 0 ));
        twistedApi.getToday().setSunset( LocalTimeUtils.getDayAsString( appLightTime.getSunset(), 0 ));

        assertEquals(LocalDateTime.parse( twistedApi.getToday().getSunrise()).toLocalDate(), LocalDate.now() );
        assertEquals(LocalDateTime.parse( twistedApi.getToday().getSunset()).toLocalDate(), LocalDate.now() );
    }

    @Test
    public void checkIfItsToday(){
        //we want to get today or tomorrows twistSunrise and twistSunset but we might not be online..
        //what do we do?
        String yesterdaySunrise = "2017-10-26T12:07:26+00:00";
        String yesterdaySunset = "2017-10-26T23:03:42+00:00";

        LightTime appLightTime = twistedStorage.getLightTime();

        appLightTime.setSunrise( yesterdaySunrise );
        appLightTime.setSunset( yesterdaySunset );

        String now = "2017-10-26T23:03:42+00:00";
        DateTime nowDateTime = new DateTime( now );

        assertTrue( new DateTime( yesterdaySunrise ).toLocalDate().equals( nowDateTime.toLocalDate() ));
        assertTrue( LocalTimeUtils.isSameDay( yesterdaySunrise, now ));
        assertFalse( LocalTimeUtils.isSameDay( yesterdaySunrise, "" ));

        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );
    }

    /**
     * The app has twistSunrise and twistSunset from yesterday.
     * We are requesting today's. Do we need new data?
     */
    @Test
    public void testCheckIfCacheIsNeeded(){
        String yesterdaySunrise = "2017-10-26T12:07:26+00:00";
        String yesterdaySunset = "2017-10-26T23:03:42+00:00";

        LightTime appLightTime = twistedStorage.getLightTime();

        appLightTime.setSunrise( yesterdaySunrise );
        appLightTime.setSunset( yesterdaySunset );

        CoreLightApi proxy = new CoreLightApi( m );
        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        proxy.generateTodayTimeLight( response );
        assertEquals( proxyResult[0].getSunrise(), twistedApi.getToday().getSunrise() );
        verify( twistedApi.asMocked() ).generateTodayTimeLight( any(Response.class));

        reset( twistedApi.asMocked() );

        //app is up to date, so for our next proxy request we should get a cached version.
        appLightTime.setSunrise( LocalTimeUtils.getDayAsString(yesterdaySunrise, 0));
        appLightTime.setSunset( LocalTimeUtils.getDayAsString(yesterdaySunset, 0));
        proxy.generateTodayTimeLight( response );

        //so our twistedApi.asMocked() shouldn't have been called
        verify( twistedApi.asMocked(), times(0) ).generateTodayTimeLight( any(Response.class));
    }

    /**
     * we want to find twistSunrise and twistSunset for today, but we are offline..
     * lets test if we can copy the one from the day before
     */
    @Test
    public void testGettingTodayScheduleOffline(){
        twistedNetwork.isOnline = false;

        String yesterdaySunrise = "2017-10-26T12:07:26+00:00";
        String yesterdaySunset = "2017-10-26T23:03:42+00:00";

        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( yesterdaySunrise );
        appLightTime.setSunset( yesterdaySunset );

        CoreLightApi proxy = new CoreLightApi( m );

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        proxy.generateTodayTimeLight( response );
        verify( twistedApi.asMocked(), times(0) ).generateTodayTimeLight( any(Response.class));
        assertEquals( proxyResult[0].getSunrise(), LocalTimeUtils.getDayAsString(yesterdaySunrise, 0));
        assertEquals( proxyResult[0].getSunset(), LocalTimeUtils.getDayAsString(yesterdaySunset, 0));

        proxy.generateTomorrowTimeLight( response );
        verify( twistedApi.asMocked(), times(0) ).generateTodayTimeLight( any(Response.class));
        assertEquals( proxyResult[0].getSunrise(), LocalTimeUtils.getDayAsString(yesterdaySunrise, 1));
        assertEquals( proxyResult[0].getSunset(), LocalTimeUtils.getDayAsString(yesterdaySunset, 1));

        //sweet, we are now aware our proxy is generating an old date into a new date when there is no network.
        //what if there is no data set, meaning it's the first time and we are not in the network
        appLightTime.setSunrise("");
        appLightTime.setSunset("");
        proxy.generateTodayTimeLight( response );
        assertFalse(LightTimeUtils.isValid( proxyResult[0]));

        proxy.generateTomorrowTimeLight( response );
        assertFalse(LightTimeUtils.isValid( proxyResult[0]));

        //knowing that our response has an invalid appLightTime means
        //we can use this flag to make a special case.
    }

    @Test
    public void testWithNetworkIssues(){
        twistedNetwork.isOnline = false;
        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise("");
        appLightTime.setSunset("");

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        planner.provideNextTimeLight( response );
        assertFalse(LightTimeUtils.isValid( proxyResult[0]));

        //if we have a network, but we don't have location permissions
        twistedNetwork.isOnline = true;
        twistedLS.isGranted = false;

        planner.provideNextTimeLight( response );
        assertFalse(LightTimeUtils.isValid( proxyResult[0]));
    }

    @Test
    public void testWithOnlineIssuesForToday(){

        String notTodaySunrise = "2017-10-26T12:07:26+00:00";
        String notTodaySunset = "2017-10-26T23:03:42+00:00";

        //this is the data stored.. from another day
        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( notTodaySunrise );
        appLightTime.setSunset( notTodaySunset );

        twistedNetwork.isOnline = true;
        m.applyNow( LocalTime.parse("12:00:00") );

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        planner.provideNextTimeLight( response );

        //we should have schedule tomorrow sunrise.
        assertEquals( proxyResult[0].getNextSchedule(), LocalTimeUtils.getDayAsString( notTodaySunset, 0 ) );
    }


    /**
     * we have cached another day's lightTime, but while being
     * online there is an issue getting the data.
     * We expect to used what has been cached.
     */
    @Test
    public void testWithOnlineIssuesForTomorrow(){

        String notTodaySunrise = "2017-10-26T12:07:26+00:00";
        String notTodaySunset = "2017-10-26T23:03:42+00:00";

        //this is the data stored.. from another day
        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( notTodaySunrise );
        appLightTime.setSunset( notTodaySunset );

        twistedNetwork.isOnline = true;
        m.applyNow( LocalTime.parse("23:00:00") );

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        planner.provideNextTimeLight( response );

        //we should have schedule tomorrow sunrise.
        assertEquals( proxyResult[0].getNextSchedule(), LocalTimeUtils.getDayAsString( notTodaySunrise, 1 ) );

        //also ensure we have the lightTime for today
        assertEquals( planner.getTodayLightTime().getSunrise(), LocalTimeUtils.getDayAsString( notTodaySunrise, 0 ) );
        assertEquals( planner.getTodayLightTime().getSunset(), LocalTimeUtils.getDayAsString( notTodaySunset, 0 ) );
    }


}