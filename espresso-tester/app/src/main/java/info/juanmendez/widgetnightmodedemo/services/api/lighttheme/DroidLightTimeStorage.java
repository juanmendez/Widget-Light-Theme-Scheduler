package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.services.LightTimeStorage;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;


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

    private LightTime mLightTime;

    @Override
    public LightTime getLightTime() {
      return mLightTime;
    }

    @Override
    public void saveLightTime(LightTime lightTime) {
        LightTimeUtils.copy( lightTime, mLightTime );
    }
}