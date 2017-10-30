package info.juanmendez.daynightthemescheduler;

import android.location.Location;

import junit.framework.Assert;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightThemePlanner;
import info.juanmendez.daynightthemescheduler.services.LightThemeApi;
import info.juanmendez.daynightthemescheduler.services.LightLocationService;
import info.juanmendez.daynightthemescheduler.services.LightThemeNetworkService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * LightThemeScheduler based on several conditions figures out if there is a schedule to carry over
 */
public class LightThemeSchedulerTest {
    LocalTime twistSunrise;
    LocalTime twistSunset;

    LightThemeApi apiRetro;
    LightTime appLightTime;
    LightTime twistApiToday;
    LightTime twistApiTomorrow;

    LightLocationService locationService;

    boolean twistIsOnline = true;
    boolean twistLocationGranted = true;
    LightThemeNetworkService networkService;
    LightThemeModule m;

    LightThemeClient client;

    @Before
    public void onBefore(){

        appLightTime = new LightTime();
        twistApiToday = new LightTime();
        twistApiTomorrow = new LightTime();

        twistSunrise = LocalTime.now();
        twistSunset = LocalTime.now();

        generateProxy();
        generateNetworkService();
        generateLocationService();
        generateClient();

        m = LightThemeModule.create()
                .applyLighTimeApi( apiRetro )
                .applyLocationService(locationService)
                .applyNetworkService(networkService)
                .applyLightTime( appLightTime )
                .applyNow( LocalTime.now() );
    }

    private void generateClient() {

    }

    private void generateNetworkService() {
        networkService = mock( LightThemeNetworkService.class );
        doAnswer(invocation -> twistIsOnline).when( networkService ).isOnline();
    }

    private void generateProxy() {
        //chicago.. https://api.sunrise-sunset.org/json?lat=41.8500300&lng=-87.6500500&formatted=0
        apiRetro = mock( LightThemeApi.class );

        doAnswer(invocation -> {
            Response<LightTime> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(twistApiToday);
            return null;
        }).when(apiRetro).generateTodayTimeLight(any(Response.class));

        doAnswer(invocation -> {
            Response<LightTime> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(twistApiTomorrow);
            return null;
        }).when(apiRetro).generateTomorrowTimeLight(any(Response.class));
    }

    private void generateLocationService(){
        locationService = mock( LightLocationService.class );
        doAnswer(invocation -> twistLocationGranted).when( locationService ).isGranted();

        Location location = mock(Location.class);
        doReturn(0d).when( location ).getLatitude();
        doReturn(0d).when( location ).getLongitude();

        doReturn( location ).when( locationService ).getLastKnownLocation();
    }

    @Test
    public void testScheduler(){
        LightThemeScheduler scheduler = new LightThemeScheduler();

        LightThemePlanner planner = Whitebox.getInternalState( scheduler, "planner");

        final LightTime[] proxyResult = new LightTime[1];

        Response<LightTime> response = result -> {
            proxyResult[0] = result;
        };

        twistApiToday.setSunrise( "2017-10-27T12:07:26+00:00" );
        twistApiToday.setSunset( "2017-10-27T23:03:42+00:00" );

        twistApiTomorrow.setSunrise( "2017-10-28T12:07:26+00:00" );
        twistApiTomorrow.setSunset( "2017-10-28T23:03:42+00:00" );

        //it's 5am
        m.applyNow( LocalTime.parse("5:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistApiToday.getSunrise() );


        //it's 2pm
        m.applyNow( LocalTime.parse("14:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistApiToday.getSunset() );


        //it's 11pm
        m.applyNow( LocalTime.parse("23:00:00"));
        planner.provideNextTimeLight( response );
        Assert.assertEquals( proxyResult[0].getNextSchedule(), twistApiTomorrow.getSunrise() );
    }
}
