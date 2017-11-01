package info.juanmendez.daynightthemescheduler.twists;

import android.support.v7.app.AppCompatDelegate;

import org.mockito.Mockito;

import info.juanmendez.daynightthemescheduler.models.WidgetScreenStatus;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;

import static org.mockito.Matchers.anyInt;
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
    public int userOption = AppCompatDelegate.MODE_NIGHT_AUTO;
    public int screenMode = WidgetScreenStatus.WIDGET_DAY_SCREEN;

    @Override
    public LightWidgetService asMocked() {
        if( widgetService == null ){
            widgetService = mock( LightWidgetService.class );

            doAnswer( invocation -> widgets).when( widgetService ).getWidgetsCount();
            doAnswer( invocation -> userOption ).when( widgetService ).getWidgetScreenOption();
            doAnswer( invocation -> screenMode).when( widgetService ).getWidgetScreenMode();
            doAnswer( invocation ->{
                screenMode = invocation.getArgumentAt(0, Integer.class );
                return null;
            }).when( widgetService ).setWidgetScreenMode( anyInt());
        }

        return widgetService;
    }

    @Override
    public void reset() {
        Mockito.reset( widgetService );
    }
}