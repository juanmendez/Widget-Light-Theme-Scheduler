package info.juanmendez.daynightthemescheduler;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.powermock.reflect.Whitebox;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightPlanner;
import info.juanmendez.daynightthemescheduler.twists.TwistAlarmService;
import info.juanmendez.daynightthemescheduler.twists.TwistApi;
import info.juanmendez.daynightthemescheduler.twists.TwistLocationService;
import info.juanmendez.daynightthemescheduler.twists.TwistNetworkService;
import info.juanmendez.daynightthemescheduler.twists.TwistStorage;
import info.juanmendez.daynightthemescheduler.twists.TwistWidgetService;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

abstract public class LightThemeTest {

    LightTime appLightTime;
    TwistAlarmService twistedAlarm;
    TwistApi twistedApi;
    TwistLocationService twistedLocationService;
    TwistNetworkService twistedNetwork;
    TwistStorage twistedStorage;
    TwistWidgetService twistedWidgetService;

    LightThemeModule m;
    LightThemeClient client;
    LightPlanner planner;

    @Before
    public void onBefore(){

        appLightTime = new LightTime();
        twistedAlarm = new TwistAlarmService();
        twistedApi = new TwistApi();
        twistedLocationService = new TwistLocationService();
        twistedNetwork = new TwistNetworkService();
        twistedStorage = new TwistStorage();
        twistedWidgetService = new TwistWidgetService();

        m = LightThemeModule.create()
                .applyLighTimeApi( twistedApi.asMocked() )
                .applyLocationService(twistedLocationService.asMocked())
                .applyNetworkService(twistedNetwork.asMocked())
                .applyLightTime( appLightTime )
                .applyNow( LocalTime.now() );

        client = new LightThemeClient(m, twistedWidgetService.asMocked(), twistedAlarm.asMocked(), twistedStorage.asMocked() );
        planner = Whitebox.getInternalState( client, "planner");
    }
}
