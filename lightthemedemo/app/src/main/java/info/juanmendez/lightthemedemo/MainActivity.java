package info.juanmendez.lightthemedemo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.lightthemedemo.services.lighttheme.DroidLocationService;
import info.juanmendez.lightthemedemo.services.lighttheme.LightManagerFactory;
import info.juanmendez.lightthemedemo.services.preferences.LightTimePrefs_;
import info.juanmendez.lightthemedemo.services.preferences.WidgetPrefs_;
import info.juanmendez.lightthemescheduler.LightThemeManager;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Pref
    WidgetPrefs_ mWidgetPrefs;

    @Pref
    LightTimePrefs_ mLightTimePrefs;

    @ViewById
    RadioGroup radioGroup;

    @ViewById
    RadioButton autoRadioButton, dayOnlyRadioButton, nightOnlyRadioButton;

    @Bean
    LightManagerFactory clientBuilder;

    @App
    MyApp app;

    @Override
    public void onPause(){
        super.onPause();
        reflectThemeChoice( mWidgetPrefs.screenOption().get() );

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            saveThemeChoice(checkedId);
        });
    }

    private void reflectThemeChoice(int choiceMade){
        switch ( choiceMade){
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                autoRadioButton.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                dayOnlyRadioButton.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                nightOnlyRadioButton.setChecked(true);
                break;
        }
    }

    private void saveThemeChoice( int radioButtonId ){
        switch ( radioButtonId){
            case R.id.autoRadioButton:
                mWidgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_AUTO );
                checkPermissions();
                break;
            case R.id.dayOnlyRadioButton:
                mWidgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_NO );
                notifyThemeManager();
                break;
            case R.id.nightOnlyRadioButton:
                mWidgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_YES );
                notifyThemeManager();
                break;
        }
    }

    private void notifyThemeManager(){
        //these changes need to be known by ligthThemeClient
        clientBuilder.getManager().onAppEvent(LightThemeManager.THEME_OPTION_CHANGED);
    }


    private void checkPermissions(){
        if (!DroidLocationService.isLocationGranted(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else{
            notifyThemeManager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode == 1 ){
            notifyThemeManager();
        }
    }

    @Click(R.id.clearDataBtn)
    public void onClearData(){
        mWidgetPrefs.clear();
        mLightTimePrefs.clear();
    }
}
