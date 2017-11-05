package info.juanmendez.lightthemescheduler.twists;

import org.mockito.Mockito;

import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.services.LightTimeStorage;
import info.juanmendez.lightthemescheduler.utils.LightTimeUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistStorage implements Twist<LightTimeStorage> {
    LightTimeStorage mStorage;
    LightTime mLightTime = new LightTime();

    @Override
    public LightTimeStorage asMocked() {
        if( mStorage == null ){
            mStorage = mock( LightTimeStorage.class );
            doAnswer( invocation -> mLightTime).when(mStorage).getLightTime();

            doAnswer( invocation -> {
                LightTime theirLightTime = invocation.getArgumentAt(0, LightTime.class );

                if( theirLightTime != null ){
                    LightTimeUtils.copy( theirLightTime, mLightTime);
                }else{
                    LightTimeUtils.clear(mLightTime);
                }

                return null;
            }).when(mStorage).saveLightTime( any( LightTime.class));
        }

        return mStorage;
    }

    public LightTime getLightTime() {
        return mLightTime;
    }

    public void setLightTime(LightTime lightTime) {
        LightTimeUtils.copy( lightTime, this.mLightTime);
    }

    @Override
    public void reset() {
        Mockito.reset(mStorage);
    }
}
