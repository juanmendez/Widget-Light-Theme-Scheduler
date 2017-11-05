package info.juanmendez.lightthemedemo.services.api.lighttheme;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.EBean;

import info.juanmendez.lighttheme.services.LightAlarmService;
import info.juanmendez.lightthemedemo.services.api.alarm.WidgetAlarmJob;


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
        long oneMinute = 5*60*1000L;
        long fifteenMinutes = 20*60*60*1000L;
        WidgetAlarmJob.scheduleJobWhenOnline( oneMinute, fifteenMinutes );
    }
}
