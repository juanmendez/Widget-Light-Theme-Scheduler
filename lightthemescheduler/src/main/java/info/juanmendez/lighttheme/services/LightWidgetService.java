package info.juanmendez.lighttheme.services;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightWidgetService {
    int getWidgetsCount();

    /**
     * gets the option placed by the user
     * night-only, day-only, night-auto
     * @return
     */
    int getWidgetScreenOption();

    //gets the last option stored in the device
    int getWidgetScreenMode();

    /**
     * Library places the value for the in day or night mode
     * @param screenMode
     */
    void setWidgetScreenMode(int screenMode );
}
