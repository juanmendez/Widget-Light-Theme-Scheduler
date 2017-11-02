package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import timber.log.Timber;


/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidAlarmService implements LightAlarmService {
    @Override
    public void cancelIfRunning() {
        Timber.i( "cancel if an alarm is running");
    }

    @Override
    public void scheduleNext(LightTime lightTime) {
        Timber.i( "Schedule next " + lightTime );
    }

    @Override
    public void scheduleNextWhenOnline() {
        Timber.i( "schedule next time device is online");
    }
}
