package info.juanmendez.lighttheme.twists;

import org.mockito.Mockito;

import info.juanmendez.lighttheme.services.LightNetworkService;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistNetworkService implements Twist<LightNetworkService> {

    private LightNetworkService mNetworkService;
    public boolean isOnline = true;

    @Override
    public LightNetworkService asMocked() {
        if( mNetworkService == null ){
            mNetworkService = mock( LightNetworkService.class );
            doAnswer(invocation -> isOnline).when(mNetworkService).isOnline();
        }
        return mNetworkService;
    }

    @Override
    public void reset() {
        Mockito.reset(mNetworkService);
    }
}
