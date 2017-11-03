package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import android.content.Context;
import android.location.Location;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.services.LightLocationService;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidLocationService implements LightLocationService {

    private static boolean sIsGranted = true;

    @Override
    public boolean isGranted() {
        return sIsGranted;
    }

    @Override
    public Location getLastKnownLocation() {
        return null;
    }

    public static boolean isLocationGranted( Context context ){
        return sIsGranted;
    }

    public static boolean isIsGranted() {
        return sIsGranted;
    }

    public static void setIsGranted(boolean isGranted) {
        sIsGranted = isGranted;
    }
}