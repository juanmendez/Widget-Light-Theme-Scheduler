package info.juanmendez.lighttheme.twists;

import android.location.Location;

import org.mockito.Mockito;

import info.juanmendez.lighttheme.services.LightLocationService;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistLocationService implements Twist<LightLocationService> {

    public boolean sIsGranted = true;
    LightLocationService mLocationService;

    @Override
    public LightLocationService asMocked() {

        if( mLocationService == null ){
            mLocationService = mock( LightLocationService.class );
            doAnswer(invocation -> sIsGranted).when(mLocationService).isGranted();

            Location location = mock(Location.class);
            doReturn(0d).when( location ).getLatitude();
            doReturn(0d).when( location ).getLongitude();

            doAnswer( invocation -> {
                if(sIsGranted){
                    return location;
                }else{
                    throw new Exception("LocationService requires permission");
                }

            } ).when(mLocationService).getLastKnownLocation();
        }

        return mLocationService;
    }

    @Override
    public void reset() {
        Mockito.reset(mLocationService);
    }
}
