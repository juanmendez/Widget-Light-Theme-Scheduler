package info.juanmendez.lightthemedemo;

import android.app.Application;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.EApplication;

import info.juanmendez.lightthemedemo.services.alarm.WidgetAlarmCreator;
import timber.log.Timber;


/**
 * Created by Juan Mendez on 10/11/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EApplication
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant( new Timber.DebugTree());
        JobManager.create(this).addJobCreator( new WidgetAlarmCreator() );
    }
}
