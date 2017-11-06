package info.juanmendez.lightthemedemo.services.alarm;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import info.juanmendez.lightthemescheduler.LightThemeManager;
import info.juanmendez.lightthemedemo.services.lighttheme.LightManagerFactory;


/**
 * Created by Juan Mendez on 11/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class WidgetAlarmJob extends Job {

    public static final String TAG = WidgetAlarmJob.class.getName() + "_Alarm";
    public static final String ACTION = WidgetAlarmJob.class.getName() + "_Action";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        PersistableBundleCompat extras = params.getExtras();
        String action = extras.getString(ACTION, "");

        if( !action.isEmpty()){
            LightManagerFactory.getManager(getContext()).onAppEvent( action );
        }

        return !action.isEmpty()? Result.SUCCESS:Result.FAILURE;
    }

    public static void scheduleJobAroundGivenTime(long timeFromNow ){
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString( ACTION, LightThemeManager.ALARM_EXECUTED);

        new JobRequest.Builder(TAG)
                .setExecutionWindow( timeFromNow, timeFromNow + (5*60*1000) )
                .setUpdateCurrent(true)
                .setExtras( extras )
                .build()
                .schedule();
    }

    public static void scheduleJobWhenOnline( long timeFromNow, long timeFromNowAtTheLatest ){

        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString( ACTION, LightThemeManager.ALARM_EXECUTED_ONLINE);

        new JobRequest.Builder(TAG)
                .setExecutionWindow( timeFromNow, timeFromNowAtTheLatest )
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .setUpdateCurrent(true)
                .setExtras( extras )
                .build()
                .schedule();
    }
}