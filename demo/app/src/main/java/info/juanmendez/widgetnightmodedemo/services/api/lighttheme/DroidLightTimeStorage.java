package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;
import info.juanmendez.widgetnightmodedemo.LightTimePrefs_;


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
    LightTimePrefs_ pref;

    @Override
    public LightTime getLightTime() {
      LightTime lightTime = new LightTime( pref.sunrise().get(), pref.sunset().get(), pref.nextSchedule().get(), pref.status().get() );
      return lightTime;
    }

    @Override
    public void saveLightTime(LightTime lightTime) {
        pref.sunrise().put( lightTime.getSunrise() );
        pref.sunset().put( lightTime.getSunset() );
        pref.nextSchedule().put( lightTime.getNextSchedule() );
        pref.status().put( lightTime.getStatus() );
    }
}