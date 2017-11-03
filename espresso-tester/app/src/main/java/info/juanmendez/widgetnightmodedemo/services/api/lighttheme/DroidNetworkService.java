package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.services.LightNetworkService;


/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidNetworkService implements LightNetworkService {
    private static boolean sIsOnline = true;

    @Override
    public boolean isOnline() {
        return sIsOnline;
    }

    public static boolean isNetworkOnline() {
        return sIsOnline;
    }

    public static void setNetworkOnline(boolean isOnline) {
        sIsOnline = isOnline;
    }
}
