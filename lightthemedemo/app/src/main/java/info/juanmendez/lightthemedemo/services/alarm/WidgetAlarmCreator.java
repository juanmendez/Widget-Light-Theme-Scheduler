package info.juanmendez.lightthemedemo.services.alarm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;


/**
 * Created by Juan Mendez on 11/2/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class WidgetAlarmCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        if( tag.equals( WidgetAlarmJob.TAG )){
            return new WidgetAlarmJob();
        }

        return null;
    }
}
