package info.juanmendez.daynightthemescheduler.models;

import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.services.LightApi;
import info.juanmendez.daynightthemescheduler.services.LightLocationService;
import info.juanmendez.daynightthemescheduler.services.LightNetworkService;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeModule {

    LightNetworkService networkService;
    LightLocationService locationService;
    LightApi lightTimeApi;
    LightTime lightTime;
    LocalTime now;

    public static LightThemeModule create(){
        return new LightThemeModule();
    }

    public LightThemeModule applyNetworkService(LightNetworkService networkService) {
        this.networkService = networkService;
        return this;
    }

    public LightThemeModule applyLocationService(LightLocationService locationService) {
        this.locationService = locationService;
        return this;
    }

    public LightThemeModule applyLighTimeApi(LightApi lightTimeApi) {
        this.lightTimeApi = lightTimeApi;
        return this;
    }

    public LightThemeModule applyLightTime(LightTime lightTime) {
        this.lightTime = lightTime;
        return this;
    }

    public LightThemeModule applyNow( LocalTime now ){
        this.now = now;
        return this;
    }

    public LightNetworkService getNetworkService() {
        return networkService;
    }

    public LightLocationService getLocationService() {
        return locationService;
    }

    public LightApi getLightTimeApi() {
        return lightTimeApi;
    }

    public LightTime getLightTime() {
        return lightTime;
    }

    public LocalTime getNow() {
        return now;
    }
}
