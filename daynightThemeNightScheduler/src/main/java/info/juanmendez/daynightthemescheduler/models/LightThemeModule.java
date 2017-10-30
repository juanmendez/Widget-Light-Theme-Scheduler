package info.juanmendez.daynightthemescheduler.models;

import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.services.LightThemeApi;
import info.juanmendez.daynightthemescheduler.services.LightLocationService;
import info.juanmendez.daynightthemescheduler.services.LightThemeNetworkService;

/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightThemeModule {

    LightThemeNetworkService networkService;
    LightLocationService locationService;
    LightThemeApi lightTimeApi;
    LightTime lightTime;
    LocalTime now;

    public static LightThemeModule create(){
        return new LightThemeModule();
    }

    public LightThemeModule applyNetworkService(LightThemeNetworkService networkService) {
        this.networkService = networkService;
        return this;
    }

    public LightThemeModule applyLocationService(LightLocationService locationService) {
        this.locationService = locationService;
        return this;
    }

    public LightThemeModule applyLighTimeApi(LightThemeApi lightTimeApi) {
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

    public LightThemeNetworkService getNetworkService() {
        return networkService;
    }

    public LightLocationService getLocationService() {
        return locationService;
    }

    public LightThemeApi getLightTimeApi() {
        return lightTimeApi;
    }

    public LightTime getLightTime() {
        return lightTime;
    }

    public LocalTime getNow() {
        return now;
    }
}
