package info.juanmendez.widgetnightmodedemo.services;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;


/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidAlarmService implements LightAlarmService {
    @Override
    public void cancelIfRunning() {

    }

    @Override
    public void scheduleNext(LightTime lightTime) {

    }

    @Override
    public void scheduleNextWhenOnline() {

    }
}
