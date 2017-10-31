package info.juanmendez.widgetnightmodedemo;

import android.app.Application;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import timber.log.Timber;


/**
 * Created by Juan Mendez on 10/11/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@EApplication
public class MyApp extends Application {

    @Pref
    ThemePrefs_ themePrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant( new Timber.DebugTree());
    }
}
