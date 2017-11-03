package info.juanmendez.daynightthemescheduler.models;

import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.services.LightApi;
import info.juanmendez.daynightthemescheduler.services.LightLocationService;
import info.juanmendez.daynightthemescheduler.services.LightNetworkService;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeModule {

    LightNetworkService mNetworkService;
    LightLocationService mLocationService;
    LightApi mLightTimeApi;
    LightTimeStorage mStorage;
    LocalTime mNow;

    public static LightThemeModule create(){
        return new LightThemeModule();
    }

    public LightThemeModule applyNetworkService(LightNetworkService networkService) {
        mNetworkService = networkService;
        return this;
    }

    public LightThemeModule applyLocationService(LightLocationService locationService) {
        mLocationService = locationService;
        return this;
    }

    public LightThemeModule applyLighTimeApi(LightApi lightTimeApi) {
        mLightTimeApi = lightTimeApi;
        return this;
    }

    public LightThemeModule applyLightTimeStorage(LightTimeStorage lightTimeStorage) {
        mStorage = lightTimeStorage;
        return this;
    }

    public LightThemeModule applyNow( LocalTime now ){
        mNow = now;
        return this;
    }

    public LightNetworkService getNetworkService() {
        return mNetworkService;
    }

    public LightLocationService getLocationService() {
        return mLocationService;
    }

    public LightApi getLightTimeApi() {
        return mLightTimeApi;
    }

    public LightTimeStorage getLightTimeStorage() {
        return mStorage;
    }

    public LightTime getLightTime(){
        return mStorage.getLightTime();
    }

    public LocalTime getNow() {
        return mNow;
    }
}
