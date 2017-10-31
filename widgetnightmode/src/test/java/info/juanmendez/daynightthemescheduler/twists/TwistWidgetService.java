package info.juanmendez.daynightthemescheduler.twists;

import android.support.v7.app.AppCompatDelegate;

import info.juanmendez.daynightthemescheduler.services.LightWidgetService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistWidgetService implements Twist<LightWidgetService> {

    private LightWidgetService widgetService;
    public int widgets = 0;
    public int action = AppCompatDelegate.MODE_NIGHT_AUTO;

    @Override
    public LightWidgetService asMocked() {
        if( widgetService == null ){
            widgetService = mock( LightWidgetService.class );
            doAnswer( invocation -> widgets).when( widgetService ).getWidgetsCount();
            doAnswer( invocation -> action).when( widgetService ).getNightMode();
            doNothing().when( widgetService ).updateWidgets();
        }
        return widgetService;
    }
}
