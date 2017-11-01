package info.juanmendez.widgetnightmodedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.daynightthemescheduler.LightThemeClient;
import info.juanmendez.widgetnightmodedemo.services.api.lighttheme.LightClientBuilder;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Pref
    WidgetPrefs_ widgetPrefs;

    @ViewById
    RadioGroup radioGroup;

    @ViewById
    RadioButton autoRadioButton, dayOnlyRadioButton, nightOnlyRadioButton;

    @Bean
    LightClientBuilder clientBuilder;

    @App
    MyApp app;

    @AfterViews
    public void afterViews(){

        reflectThemeChoice( widgetPrefs.screenOption().get() );

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            saveThemeChoice(checkedId);
        });
    }

    @Click
    public void goToSecondBtn(){
        startActivity( new Intent(this, SecondActivity_.class) );
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
                widgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_AUTO );
                break;
            case R.id.dayOnlyRadioButton:
                widgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_NO );
                break;
            case R.id.nightOnlyRadioButton:
                widgetPrefs.screenOption().put( AppCompatDelegate.MODE_NIGHT_YES );
                break;
        }

        //these changes need to be known by ligthThemeClient
        clientBuilder.getClient().onAppEvent(LightThemeClient.NIGHT_AUTO_CHANGED );
    }
}
