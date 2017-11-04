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
 * This is a module used by the library in order to access different dependencies found in the application.
 * This library doesn't demand any Dependency Injection library installed.
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

    /**
     * NetworkService is useful for LightApi to find out if the user is online or not.
     * This is required only if Api makes use of rest calls.
     * @param networkService
     * @return
     */
    public LightThemeModule applyNetworkService(LightNetworkService networkService) {
        mNetworkService = networkService;
        return this;
    }

    /**
     * Provides information whether the application has granted permission to find the location of the user
     * It also provides the user coordinates.
     * @param locationService
     * @return
     */
    public LightThemeModule applyLocationService(LightLocationService locationService) {
        mLocationService = locationService;
        return this;
    }

    /**
     * This class generates a LightTime object having sunrise and sunset.
     * It's up to the app to make use of rest calls or use other helpful libraries
     * to obtain this information.
     * @param lightTimeApi
     * @return
     */
    public LightThemeModule applyLighTimeApi(LightApi lightTimeApi) {
        mLightTimeApi = lightTimeApi;
        return this;
    }

    /**
     * LightTimeStorage is a factory which provides a LightTime object stored in the app.
     * It can also stored new data provided by this library.
     * @param lightTimeStorage
     * @return
     */
    public LightThemeModule applyLightTimeStorage(LightTimeStorage lightTimeStorage) {
        mStorage = lightTimeStorage;
        return this;
    }

    /**
     * In this way of having now provided, it can be helpful when testing an application.
     * @param now
     * @return
     */
    public LightThemeModule applyNow( LocalTime now ){
        mNow = now;
        return this;
    }

    public LightNetworkService getNetworkService() {

        /**
         * LightNetworkService might not be required in the app,
         * if LightApi doesn't make Rest calls.
         */
        if( mNetworkService == null ){
            mNetworkService = () -> true;
        }

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
