package info.juanmendez.lighttheme.services;

import info.juanmendez.lighttheme.models.LightTime;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This is usually done by SharedPreferences. It is suppose to store Sunrise, Sunset times
 * as well as the nextSchedule
 */

public interface LightTimeStorage {
    LightTime getLightTime();
    void saveLightTime(LightTime lightTime );
}
