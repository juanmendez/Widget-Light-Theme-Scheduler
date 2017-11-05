package info.juanmendez.lighttheme.services;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightAlarmService {
    void cancelIfRunning(); //cancel alarmService if it's running
    void scheduleNext(long msFromNow ); //provide next schedule
    void scheduleNextWhenOnline(); //try to schedule again when online
}