package info.juanmendez.widgetnightmodedemo;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.DroidLocationService;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EActivity(R.layout.activity_second)
public class ConfigActivity extends AppCompatActivity {

    @App
    MyApp app;

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
                    1);

        }else{
            onPermissionResult(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode == 1 ){
            onPermissionResult(true);
        }else{
            onPermissionResult( false );
        }
    }

    private void onPermissionResult( boolean thereIsPermission ){

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int widgetId;

        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if ( widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                setResult( RESULT_OK );
            }
        }
        
        finish();
    }
}
