package info.juanmendez.lightthemescheduler.services;

import android.location.Location;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightLocationService {
    boolean isGranted();
    Location getLocation();
}
