package info.juanmendez.lightthemedemo.services.preferences;

import android.support.v7.app.AppCompatDelegate;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import info.juanmendez.lightthemescheduler.models.WidgetScreenStatus;

/**
 * Created by Juan Mendez on 10/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface WidgetPrefs {

    /**
     * saves user option night-only, day-only, night-auto
     * @return
     */
    @DefaultInt(AppCompatDelegate.MODE_NIGHT_NO)
    int screenOption();

    /**
     * library stores the option to have the screen in day or nightmode
     * @return
     */
    @DefaultInt(WidgetScreenStatus.WIDGET_DAY_SCREEN)
    int screenMode();
}