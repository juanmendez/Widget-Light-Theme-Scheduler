package info.juanmendez.daynightthemescheduler.twists;

import android.location.Location;

import info.juanmendez.daynightthemescheduler.services.LightLocationService;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistLocationService implements Twist<LightLocationService> {

    public boolean isGranted = true;
    LightLocationService locationService;

    @Override
    public LightLocationService asMocked() {

        if( locationService == null ){
            locationService = mock( LightLocationService.class );
            doAnswer(invocation -> isGranted).when( locationService ).isGranted();

            Location location = mock(Location.class);
            doReturn(0d).when( location ).getLatitude();
            doReturn(0d).when( location ).getLongitude();

            doAnswer( invocation -> {
                if( isGranted ){
                    return location;
                }else{
                    throw new Exception("LocationService requires permission");
                }

            } ).when( locationService ).getLastKnownLocation();
        }

        return locationService;
    }
}
