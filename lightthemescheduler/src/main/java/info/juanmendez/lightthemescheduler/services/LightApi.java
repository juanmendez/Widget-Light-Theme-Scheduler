package info.juanmendez.lightthemescheduler.services;

import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.Response;

/**
 * Created by Juan Mendez on 10/17/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightApi {
    void generateTodayTimeLight(Response<LightTime> response);
    void generateTomorrowTimeLight(Response<LightTime> response );
}
