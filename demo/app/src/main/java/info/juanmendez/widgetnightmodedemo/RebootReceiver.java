package info.juanmendez.widgetnightmodedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.LightClientBuilder;
import timber.log.Timber;


/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EReceiver
public class RebootReceiver extends BroadcastReceiver {
    @Bean
    LightClientBuilder clientBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i( "Notify client about %s", intent.getAction() );
        clientBuilder.getClient().onAppEvent( intent.getAction() );
    }
}
