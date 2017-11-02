package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.services.LightAlarmService;
import info.juanmendez.widgetnightmodedemo.services.api.alarm.WidgetAlarmJob;


/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidAlarmService implements LightAlarmService {
    @Override
    public void cancelIfRunning() {
        JobManager.instance().cancelAllForTag(WidgetAlarmJob.TAG);
    }

    @Override
    public void scheduleNext(long msFromNow) {
        WidgetAlarmJob.scheduleJobAtAGivenTime( msFromNow );
    }

    @Override
    public void scheduleNextWhenOnline() {
        long fifteenMinutes = 2*60*1000L;
        long tenHours = 10*60*60*1000L;
        WidgetAlarmJob.scheduleJobWhenOnline( fifteenMinutes, tenHours );
    }
}
