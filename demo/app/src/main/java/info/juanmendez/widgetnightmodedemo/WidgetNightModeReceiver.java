package info.juanmendez.widgetnightmodedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

import info.juanmendez.widgetnightmodedemo.services.api.alarm.WidgetAlarmJob;
import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.LightClientBuilder;
import timber.log.Timber;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EReceiver
public class WidgetNightModeReceiver extends BroadcastReceiver {

    @Bean
    LightClientBuilder clientBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean validCall = true;

        if( intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) )
            validCall = JobManager.instance().getAllJobRequestsForTag(WidgetAlarmJob.TAG).size() <= 0;

        if( validCall )
            clientBuilder.getClient().onAppEvent( intent.getAction() );
        else
            Timber.i( "Reboot event action is prevented due to an alarm job running");
    }
}