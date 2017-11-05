package info.juanmendez.lighttheme;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;

import junit.framework.Assert;

import org.joda.time.LocalTime;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import info.juanmendez.lighttheme.models.LightTime;
import info.juanmendez.lighttheme.models.LightTimeStatus;
import info.juanmendez.lighttheme.models.Response;
import info.juanmendez.lighttheme.models.WidgetScreenStatus;
import info.juanmendez.lighttheme.services.LightPlanner;
import info.juanmendez.lighttheme.utils.LightTimeUtils;
import info.juanmendez.lighttheme.utils.LocalTimeUtils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * LightThemeScheduler based on several conditions figures out if there is a schedule to carry over
 */
public class LightThemeManagerTest extends LightThemeTest{

    @Test
    public void testScheduler(){

        LightPlanner planner = Whitebox.getInternalState(manager, "mPlanner");

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:26+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:42+00:00" );

        //it's 5am
        m.applyTestableNow( LocalTime.parse("5:00:00"));
        planner.generateLightTime( response );
        assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getToday().getSunrise() );


        //it's 2pm
        m.applyTestableNow( LocalTime.parse("14:00:00"));
        planner.generateLightTime( response );
        assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getToday().getSunset() );


        //it's 11pm
        m.applyTestableNow( LocalTime.parse("23:00:00"));
        planner.generateLightTime( response );
        assertEquals( proxyResult[0].getNextSchedule(), twistedApi.getTomorrow().getSunrise() );
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

        planner.generateLightTime( response );

        //lightTime storage has no data, therefore proxyResult[0] is also invalid
        //in this case LightAlarmService must set an alarm when there is network, and we can calculate..
        Assert.assertFalse( LightTimeUtils.isValid(twistedStorage.asMocked().getLightTime()));
        Assert.assertFalse(LightTimeUtils.isValid(proxyResult[0]));
        assertEquals( proxyResult[0].getStatus(), LightTimeStatus.NO_INTERNET );


        //how about online, but no permission to get location
        twistedNetwork.isOnline = true;
        twistedLS.sIsGranted = false;
        planner.generateLightTime( response );

        Assert.assertFalse( LightTimeUtils.isValid(twistedStorage.asMocked().getLightTime()));
        assertEquals( proxyResult[0].getStatus(), LightTimeStatus.NO_LOCATION_PERMISSION );


        //we must notify alarmService to make a second attempt once there is network.
        //lets pretend that it happened.
        twistedNetwork.isOnline = true;
        twistedLS.sIsGranted = true;

        //first update values returned by apiRetro
        String sunrise = "2017-10-26T12:07:26+00:00";
        String sunset = "2017-10-26T23:03:42+00:00";

        twistedApi.getToday().setSunrise(sunrise);
        twistedApi.getToday().setSunset(sunset);

        //also lets say it's two o'clock
        m.applyTestableNow( LocalTime.parse("14:00:00"));
        planner.generateLightTime( response );

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

        manager.onScheduleRequest();

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
        m.applyTestableNow( LocalTime.parse("17:50:00"));

        manager.onScheduleRequest();
        verify(twistedAlarm.asMocked()).scheduleNext( anyLong());
        assertEquals( appLightTime.getNextSchedule(), twistedApi.getToday().getSunset() );

        //11pm
        m.applyTestableNow( LocalTime.parse("23:00:00"));

        twistedAlarm.reset();
        manager.onScheduleRequest();
        verify(twistedAlarm.asMocked()).scheduleNext( anyLong() );
        assertEquals( appLightTime.getNextSchedule(), twistedApi.getTomorrow().getSunrise() );
    }

    /**
     * Scenario, we have today's sunrise/sunset, but it's 11 pm and we are offline.
     * We then make a new case of guessing based on today's schedule.
     */
    @Test
    public void testCachingForTomorrow(){

        //11pm
        m.applyTestableNow( LocalTime.parse("23:00:00"));

        LightTime appLightTime = twistedStorage.getLightTime();
        appLightTime.setSunrise( "2017-10-27T12:07:26+00:00" );
        appLightTime.setSunset( "2017-10-27T23:03:42+00:00" );

        //11pm, we are offline, therefore we are
        twistedWS.widgets = 1;
        twistedNetwork.isOnline = false;
        manager.onScheduleRequest();

        verify( twistedAlarm.asMocked() ).scheduleNext(anyLong());
        assertEquals( appLightTime.getStatus(), LightTimeStatus.LIGHTTIME_GUESSED );
        assertEquals( appLightTime.getSunrise(), LocalTimeUtils.getDayAsString("2017-10-27T12:07:26+00:00", 1) );
        assertEquals( appLightTime.getSunset(), LocalTimeUtils.getDayAsString( "2017-10-27T23:03:42+00:00", 1) );
    }

    /**
     * first time, and there is no location permission
     */
    @Test
    public void testFirstWihoutPermissions(){
        //11pm
        m.applyTestableNow( LocalTime.parse("23:00:00"));

        LightTime appLightTime = twistedStorage.getLightTime();

        //11pm, we are offline, therefore we are
        twistedWS.widgets = 1;
        twistedLS.sIsGranted = false;
        manager.onScheduleRequest();

        verify( twistedAlarm.asMocked() ).cancelIfRunning();
        assertEquals( appLightTime.getStatus(), LightTimeStatus.NO_LOCATION_PERMISSION  );
    }

    /**
     * User has gone to day mode only. lets update widgetService.
     */
    @Test
    public void whenDayOnlyOption(){
        twistedWS.widgets = 1;
        twistedWS.userOption = AppCompatDelegate.MODE_NIGHT_NO;

        manager.onAppEvent( LightThemeManager.THEME_OPTION_CHANGED );
        verify( twistedAlarm.asMocked() ).cancelIfRunning();

        //optimized, day is default.
        verify( twistedWS.asMocked(), times(0) ).setWidgetScreenMode( eq(WidgetScreenStatus.WIDGET_DAY_SCREEN) );

        twistedWS.userOption = AppCompatDelegate.MODE_NIGHT_YES;
        manager.onAppEvent( LightThemeManager.THEME_OPTION_CHANGED );
        verify( twistedAlarm.asMocked(), times(2) ).cancelIfRunning();
        verify( twistedWS.asMocked() ).setWidgetScreenMode( eq(WidgetScreenStatus.WIDGET_NIGHT_SCREEN) );
    }

    @Test
    public void whenNightAutoOptionAndNoWidgets(){

        //we have network and we have sunrise/sunset for today
        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        m.applyTestableNow( LocalTime.parse("12:00"));

        twistedWS.widgets = 0;
        twistedWS.userOption = AppCompatDelegate.MODE_NIGHT_AUTO;
        manager.onAppEvent( LightThemeManager.THEME_OPTION_CHANGED);
        verify( twistedAlarm.asMocked() ).cancelIfRunning();
        verify( twistedWS.asMocked(), times(0)).setWidgetScreenMode(anyInt() );


        twistedAlarm.reset();
        //ok, now a widget is added after.
        twistedWS.widgets = 1;
        manager.onAppEvent( AppWidgetManager.ACTION_APPWIDGET_ENABLED );
        verify( twistedAlarm.asMocked(), times(0) ).cancelIfRunning();

        //optimized call, therefore this is not call if in day mode
        verify( twistedWS.asMocked(), times(0) ).setWidgetScreenMode( eq(WidgetScreenStatus.WIDGET_DAY_SCREEN));


        //widget was removed..
        twistedWS.widgets = 0;
        manager.onAppEvent( AppWidgetManager.ACTION_APPWIDGET_DELETED );
        verify( twistedAlarm.asMocked(), times(1) ).cancelIfRunning();
    }


    /**
     * User lost her device, and when found is exactly sunset time
     * There is one widget, and last time it was in daylight mode.
     * Widget should go on night mode once the device is rebooted.
     */
    @Test
    public void whenReboot(){

        //we have network and we have sunrise/sunset for today
        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );
        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:27+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:43+00:00" );


        m.applyTestableNow( LocalTimeUtils.getLocalTime(twistedApi.getToday().getSunset() ) );

        twistedWS.widgets = 1;
        twistedWS.screenMode = WidgetScreenStatus.WIDGET_DAY_SCREEN;
        twistedWS.userOption = AppCompatDelegate.MODE_NIGHT_AUTO;
        manager.onAppEvent( Intent.ACTION_REBOOT);

        verify( twistedAlarm.asMocked() ).scheduleNext( anyLong() );
        verify( twistedWS.asMocked() ).setWidgetScreenMode( eq(WidgetScreenStatus.WIDGET_NIGHT_SCREEN) );
    }

    @Test
    public void firstWidgetAdded(){

        m.applyTestableNow( LocalTime.parse("22:00") );
        twistedApi.getToday().setSunrise( "2017-10-27T12:07:26+00:00" );
        twistedApi.getToday().setSunset( "2017-10-27T23:03:42+00:00" );

        twistedApi.getTomorrow().setSunrise( "2017-10-28T12:07:26+00:00" );
        twistedApi.getTomorrow().setSunset( "2017-10-28T23:03:42+00:00" );

        twistedWS.widgets = 1;
        manager.onAppEvent( AppWidgetManager.ACTION_APPWIDGET_ENABLED );
        verify( twistedWS.asMocked() ).setWidgetScreenMode( eq(WidgetScreenStatus.WIDGET_NIGHT_SCREEN) );
    }
}
