package info.juanmendez.lighttheme.services;

import info.juanmendez.lighttheme.models.LightTime;
import info.juanmendez.lighttheme.models.Response;

/**
 * Created by Juan Mendez on 10/17/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightApi {
    void generateTodayTimeLight(Response<LightTime> respose);
    void generateTomorrowTimeLight(Response<LightTime> response );
}
