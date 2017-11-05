package info.juanmendez.lightthemedemo.services.api.preferences;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface LightTimePrefs {
    @DefaultString("")
    String sunrise();

    @DefaultString("")
    String sunset();

    @DefaultString("")
    String nextSchedule();

    int status();

    String helloworld();
}
