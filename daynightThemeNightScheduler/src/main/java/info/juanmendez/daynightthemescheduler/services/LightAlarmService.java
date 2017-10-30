package info.juanmendez.daynightthemescheduler.services;

import info.juanmendez.daynightthemescheduler.models.LightTime;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightAlarmService {
    void cancelIfRunning(); //cancel alarmService if it's running
    void scheduleNext(LightTime lightTime ); //provide next schedule
    void scheduleNextWhenOnline(); //try to schedule again when online
}