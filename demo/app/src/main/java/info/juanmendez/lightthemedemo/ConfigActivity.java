package info.juanmendez.lightthemedemo;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import info.juanmendez.lightthemedemo.services.api.lighttheme.DroidAlarmService;
import info.juanmendez.lightthemedemo.services.api.lighttheme.DroidLocationService;
import info.juanmendez.lightthemedemo.services.api.lighttheme.LightManagerFactory;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EActivity(R.layout.activity_config)
public class ConfigActivity extends AppCompatActivity {

    @App
    MyApp app;

    @Bean
    LightManagerFactory clientBuilder;

    @Bean
    DroidAlarmService alarmService;

    int validCode = 1;

    @AfterViews
    public void afterViews(){
        checkPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void checkPermissions(){
        if (!DroidLocationService.isLocationGranted(this)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    validCode);

        }else{
            onPermissionResult(true, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onPermissionResult(requestCode==validCode, requestCode);
    }

    private void onPermissionResult( boolean thereIsPermission, int requestCode ){

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int widgetId;

        /**
         * What happens is the first widget is added before permission.
         * So what we do instead is delay 1 s
         */
        if( requestCode == validCode ){
            alarmService.scheduleNext(1000 );
        }

        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if ( widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                setResult( RESULT_OK );
            }
        }

        finish();
    }
}
