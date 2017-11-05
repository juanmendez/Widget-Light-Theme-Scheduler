package info.juanmendez.lightthemescheduler.twists;

import org.mockito.Mockito;

import info.juanmendez.lightthemescheduler.services.LightAlarmService;

import static org.mockito.Mockito.mock;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistAlarmService implements Twist<LightAlarmService> {
    LightAlarmService mService;

    @Override
    public LightAlarmService asMocked() {
        if( mService == null ){
            mService = mock( LightAlarmService.class );
        }
        return mService;
    }

    @Override
    public void reset() {
        Mockito.reset(mService);
    }
}
