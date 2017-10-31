package info.juanmendez.daynightthemescheduler;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.powermock.reflect.Whitebox;

import info.juanmendez.daynightthemescheduler.models.LightThemeModule;
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

    TwistAlarmService twistedAlarm;
    TwistApi twistedApi;
    TwistLocationService twistedLS;
    TwistNetworkService twistedNetwork;
    TwistStorage twistedStorage;
    TwistWidgetService twistedWS;

    LightThemeModule m;
    LightThemeClient client;
    LightPlanner planner;

    @Before
    public void onBefore(){

        twistedAlarm = new TwistAlarmService();
        twistedApi = new TwistApi();
        twistedLS = new TwistLocationService();
        twistedNetwork = new TwistNetworkService();
        twistedStorage = new TwistStorage();
        twistedWS = new TwistWidgetService();

        m = LightThemeModule.create()
                .applyLighTimeApi( twistedApi.asMocked() )
                .applyLocationService(twistedLS.asMocked())
                .applyNetworkService(twistedNetwork.asMocked())
                .applyLightTimeStorage( twistedStorage.asMocked() )
                .applyNow( LocalTime.now() );

        client = new LightThemeClient(m, twistedWS.asMocked(), twistedAlarm.asMocked() );
        planner = Whitebox.getInternalState( client, "planner");
    }
}
