package info.juanmendez.daynightthemescheduler.services;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface LightWidgetService {
    int getObserversCount();
    void updateLightTheme( int theme );
    int getNightMode();
}
