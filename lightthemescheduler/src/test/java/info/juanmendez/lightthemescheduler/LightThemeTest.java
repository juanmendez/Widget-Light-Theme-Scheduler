package info.juanmendez.lightthemescheduler;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.powermock.reflect.Whitebox;

import info.juanmendez.lightthemescheduler.models.LightThemeModule;
import info.juanmendez.lightthemescheduler.services.LightPlanner;
import info.juanmendez.lightthemescheduler.twists.TwistAlarmService;
import info.juanmendez.lightthemescheduler.twists.TwistApi;
import info.juanmendez.lightthemescheduler.twists.TwistLocationService;
import info.juanmendez.lightthemescheduler.twists.TwistNetworkService;
import info.juanmendez.lightthemescheduler.twists.TwistStorage;
import info.juanmendez.lightthemescheduler.twists.TwistWidgetService;

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
    LightThemeManager manager;
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
                .applyLightTimeApi( twistedApi.asMocked() )
                .applyLocationService(twistedLS.asMocked())
                .applyNetworkService(twistedNetwork.asMocked())
                .applyLightTimeStorage( twistedStorage.asMocked() )
                .applyWidgetService( twistedWS.asMocked() )
                .applyAlarmService( twistedAlarm.asMocked() )
                .applyTestableNow( LocalTime.now() );

        manager = new LightThemeManager(m);
        planner = Whitebox.getInternalState(manager, "mPlanner");
    }
}
