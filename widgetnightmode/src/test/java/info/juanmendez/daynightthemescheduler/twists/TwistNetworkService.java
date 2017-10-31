package info.juanmendez.daynightthemescheduler.twists;

import org.mockito.Mockito;

import info.juanmendez.daynightthemescheduler.services.LightNetworkService;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistNetworkService implements Twist<LightNetworkService> {

    private LightNetworkService networkService;
    public boolean isOnline = true;

    @Override
    public LightNetworkService asMocked() {
        if( networkService == null ){
            networkService = mock( LightNetworkService.class );
            doAnswer(invocation -> isOnline).when( networkService ).isOnline();
        }
        return networkService;
    }

    @Override
    public void reset() {
        Mockito.reset( networkService );
    }
}
