package info.juanmendez.widgetnightmodedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.LightManagerFactory;

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

       if( intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) )
            managerFactory.getManager().onAppEvent( intent.getAction() );
    }
}