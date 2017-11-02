package info.juanmendez.widgetnightmodedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evernote.android.job.JobManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

import info.juanmendez.widgetnightmodedemo.services.api.alarm.WidgetAlarmJob;
import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.LightClientBuilder;

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

        //if there is an alarm schedule, then don't call with ACTION_BOOT_COMPLETED
        if( intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) )
            validCall = JobManager.instance().getAllJobRequestsForTag(WidgetAlarmJob.TAG).size() <= 0;

        if( validCall )
            clientBuilder.getClient().onAppEvent( intent.getAction() );
    }
}