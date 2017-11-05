package info.juanmendez.lightthemedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

import info.juanmendez.lightthemedemo.services.api.lighttheme.LightManagerFactory;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EReceiver
public class RebootReceiver extends BroadcastReceiver {

    @Bean
    LightManagerFactory managerFactory;

    @Override
    public void onReceive(Context context, Intent intent) {

        /**
         * This is related to AndroidJob:
         * I am rescheduling so that way if there is any expired job which happens before
         * the devide is rebooted, then there is still a requirement to schedule the next sunrise or sunset.
         */
        managerFactory.getManager().onAppEvent( intent.getAction() );
    }
}