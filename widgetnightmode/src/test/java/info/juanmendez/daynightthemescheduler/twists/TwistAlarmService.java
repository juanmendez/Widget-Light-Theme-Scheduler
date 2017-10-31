package info.juanmendez.daynightthemescheduler.twists;

import info.juanmendez.daynightthemescheduler.services.LightAlarmService;

import static org.mockito.Mockito.mock;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistAlarmService implements Twist<LightAlarmService> {
    LightAlarmService service;

    @Override
    public LightAlarmService asMocked() {
        if( service == null ){
            service = mock( LightAlarmService.class );
        }
        return service;
    }
}
