package info.juanmendez.daynightthemescheduler;

import junit.framework.Assert;

import org.joda.time.LocalTime;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.LightTimeStatus;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightPlanner;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;
import info.juanmendez.daynightthemescheduler.utils.LocalTimeUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * LightThemeScheduler based on several conditions figures out if there is a schedule to carry over
 */
public class ClientTest extends LightThemeTest{

    @Test
    public void testScheduler(){

        LightPlanner planner = Whitebox.getInternalState( client, "planner");

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:26+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:42+00:00" );

        //it's 5am
        m.applyNow( LocalTime.parse("5:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getToday().getSunrise() );


        //it's 2pm
        m.applyNow( LocalTime.parse("14:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getToday().getSunset() );


        //it's 11pm
        m.applyNow( LocalTime.parse("23:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getTomorrow().getSunrise() );
    }

    @Test
    public void rebootTest(){
        //lets first ensure there is no schedule
        m.getLightTime().setNextSchedule("");

        twistedNetwork.isOnline = false;

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        planner.provideNextTimeLight( response );

        //lightTime storage has no data, therefore proxyResult[0] is also invalid
        //in this case LightAlarmService must set an alarm when there is network, and we can calculate..
        Assert.assertFalse( LightTimeUtils.isValid(twistedStorage.asMocked().getLightTime()));
        Assert.assertFalse(LightTimeUtils.isValid(proxyResult[0]));
        Assert.assertEquals( proxyResult[0].getStatus(), LightTimeStatus.NO_INTERNET );


        //how about online, but no permission to get location
        twistedNetwork.isOnline = true;
        twistedLS.isGranted = false;
        planner.provideNextTimeLight( response );

        Assert.assertFalse( LightTimeUtils.isValid(twistedStorage.asMocked().getLightTime()));
        Assert.assertEquals( proxyResult[0].getStatus(), LightTimeStatus.NO_LOCATION_PERMISSION );


        //we must notify alarmService to make a second attempt once there is network.
        //lets pretend that it happened.
        twistedNetwork.isOnline = true;
        twistedLS.isGranted = true;

        //first update values returned by apiRetro
        String sunrise = "2017-10-26T12:07:26+00:00";
        String sunset = "2017-10-26T23:03:42+00:00";

        twistedApi.getToday().setSunrise(sunrise);
        twistedApi.getToday().setSunset(sunset);

        //also lets say it's two o'clock
        m.applyNow( LocalTime.parse("14:00:00"));
        planner.provideNextTimeLight( response );

        //should be valid now..
        Assert.assertTrue(LightTimeUtils.isValid(proxyResult[0]));
    }

    @Test
    public void rebootClientTest(){
        LightTime appLightTime = twistedStorage.getLightTime();
        //lets first ensure there is no schedule
        m.getLightTime().setNextSchedule("");

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        client.onScheduleRequest();

        //online?yes, observers?no therefore no work scheduled.
        verify(twistedAlarm.asMocked()).cancelIfRunning();

        twistedWS.widgets = 1;
        twistedNetwork.isOnline = true;

        //include sunrise/sunset for today and tomorrow
        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        //tomorrow, both are carried over by a minute
        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:27+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:43+00:00" );

        //almost 6pm
        m.applyNow( LocalTime.parse("17:50:00"));

        client.onScheduleRequest();
        verify(twistedAlarm.asMocked()).scheduleNext( any(LightTime.class));
        Assert.assertEquals( appLightTime.getNextSchedule(), twistedApi.getToday().getSunset() );

        //11pm
        m.applyNow( LocalTime.parse("23:00:00"));
        twistedAlarm.reset();
        client.onScheduleRequest();
        verify(twistedAlarm.asMocked()).scheduleNext( any(LightTime.class));
        Assert.assertEquals( appLightTime.getNextSchedule(), twistedApi.getTomorrow().getSunrise() );
    }

    /**
     * Scenario, we have today's sunrise/sunset, but it's 11 pm and we are offline.
     * We then make a new case of guessing based on today's schedule.
     */
    @Test
    public void testCachingForTomorrow(){

        //11pm
        m.applyNow( LocalTime.parse("23:00:00"));

        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( "2017-10-27T12:07:26+00:00" );
        appLightTime.setSunset( "2017-10-27T23:03:42+00:00" );

        //11pm, we are offline, therefore we are
        twistedWS.widgets = 1;
        twistedNetwork.isOnline = false;
        client.onScheduleRequest();

        verify( twistedAlarm.asMocked() ).scheduleNext(any(LightTime.class));
        Assert.assertEquals( appLightTime.getStatus(), LightTimeStatus.LIGHTTIME_GUESSED );
        Assert.assertEquals( appLightTime.getSunrise(), LocalTimeUtils.getDayAsString("2017-10-27T12:07:26+00:00", 1) );
        Assert.assertEquals( appLightTime.getSunset(), LocalTimeUtils.getDayAsString( "2017-10-27T23:03:42+00:00", 1) );
    }

    /**
     * first time, and there is no location permission
     */
    @Test
    public void testFirstWihoutPermissions(){
        //11pm
        m.applyNow( LocalTime.parse("23:00:00"));

        LightTime appLightTime = twistedStorage.getLightTime();

        //11pm, we are offline, therefore we are
        twistedWS.widgets = 1;
        twistedLS.isGranted = false;
        client.onScheduleRequest();

        verify( twistedAlarm.asMocked() ).cancelIfRunning();
        Assert.assertEquals( appLightTime.getStatus(), LightTimeStatus.NO_LOCATION_PERMISSION  );
    }
}
