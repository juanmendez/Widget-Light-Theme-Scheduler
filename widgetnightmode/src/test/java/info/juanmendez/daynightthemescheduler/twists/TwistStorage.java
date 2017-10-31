package info.juanmendez.daynightthemescheduler.twists;

import org.mockito.Mockito;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistStorage implements Twist<LightTimeStorage> {
    LightTimeStorage storage;
    LightTime lightTime = new LightTime();

    @Override
    public LightTimeStorage asMocked() {
        if( storage == null ){
            storage = mock( LightTimeStorage.class );
            doAnswer( invocation -> lightTime).when( storage ).getLightTime();

            doAnswer( invocation -> {
                LightTime theirLightTime = invocation.getArgumentAt(0, LightTime.class );

                if( theirLightTime != null ){
                    LightTimeUtils.copy( theirLightTime, lightTime);
                }else{
                    LightTimeUtils.clear(lightTime);
                }

                return null;
            }).when( storage ).saveLightTime( any( LightTime.class));
        }

        return storage;
    }

    public LightTime getLightTime() {
        return lightTime;
    }

    public void setLightTime(LightTime lightTime) {
        LightTimeUtils.copy( lightTime, this.lightTime);
    }

    @Override
    public void reset() {
        Mockito.reset( storage );
    }
}
