package info.juanmendez.widgetnightmodedemo.services.api.alarm;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import info.juanmendez.daynightthemescheduler.LightThemeClient;


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
            Intent intent = new Intent(action);
            getContext().sendBroadcast( intent );
        }

        return !action.isEmpty()? Result.SUCCESS:Result.FAILURE;
    }

    public static int scheduleJobAtAGivenTime( long timeFromNow ){
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString( ACTION, LightThemeClient.SCHEDULE_COMPLETED  );

        return new JobRequest.Builder(TAG)
                .setExact( timeFromNow )
                .setUpdateCurrent(true)
                .setExtras( extras )
                .build()
                .schedule();
    }

    public static int scheduleJobWhenOnline( long timeFromNow, long timeFromNowAtTheLatest ){

        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString( ACTION, LightThemeClient.SCHEDULE_WHEN_ONLINE  );

        return new JobRequest.Builder(TAG)
                .setExecutionWindow( timeFromNow, timeFromNowAtTheLatest )
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .setUpdateCurrent(true)
                .setExtras( extras )
                .build()
                .schedule();
    }
}