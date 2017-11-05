package info.juanmendez.lightthemedemo.services.lighttheme;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.EBean;

import java.util.Date;

import info.juanmendez.lightthemescheduler.services.LightAlarmService;
import info.juanmendez.lightthemedemo.services.alarm.WidgetAlarmJob;
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
        JobManager.instance().cancelAllForTag(WidgetAlarmJob.TAG);
    }

    @Override
    public void scheduleNext(long msFromNow) {
        WidgetAlarmJob.scheduleJobAtAGivenTime( msFromNow );
        Timber.i( "Next schedule is around " + new Date( System.currentTimeMillis() + msFromNow ));
    }

    @Override
    public void scheduleNextWhenOnline() {
        long fifteenMinutes = 15*60*1000L;
        long twentyHours = 20*60*60*1000L;

        WidgetAlarmJob.scheduleJobWhenOnline( fifteenMinutes, twentyHours );
        Timber.i( "Scheduling a job to find out when device is online between %s and %s ", new Date( System.currentTimeMillis() + fifteenMinutes ), new Date( System.currentTimeMillis() + twentyHours ) );
    }
}
