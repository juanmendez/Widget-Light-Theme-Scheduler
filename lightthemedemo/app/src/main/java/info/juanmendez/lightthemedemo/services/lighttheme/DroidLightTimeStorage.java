package info.juanmendez.lightthemedemo.services.lighttheme;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.lighttheme.models.LightTime;
import info.juanmendez.lighttheme.services.LightTimeStorage;
import info.juanmendez.lightthemedemo.services.preferences.LightTimePrefs_;


/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This class is a setter and getter for sharePreferences which mirrors the
 * attribute names in LightTime class. In this way LightThemeClient has a hold of the values.
 */
@EBean
public class DroidLightTimeStorage implements LightTimeStorage {

    @Pref
    LightTimePrefs_ prefs;

    @Override
    public LightTime getLightTime() {
      LightTime lightTime = new LightTime();
      lightTime.setSunrise(prefs.sunrise().get());
      lightTime.setSunset(prefs.sunset().get());
      lightTime.setNextSchedule( prefs.nextSchedule().get() );
      lightTime.setStatus(prefs.status().get());
      return lightTime;
    }

    @Override
    public void saveLightTime(LightTime lightTime) {
        prefs.sunrise().put( lightTime.getSunrise() );
        prefs.sunset().put( lightTime.getSunset() );
        prefs.nextSchedule().put( lightTime.getNextSchedule() );
        prefs.status().put( lightTime.getStatus() );
    }
}