package info.juanmendez.lightthemedemo.services.lighttheme;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.lightthemescheduler.models.WidgetScreenStatus;
import info.juanmendez.lightthemescheduler.services.LightWidgetService;
import info.juanmendez.lightthemedemo.WidgetProvider_;
import info.juanmendez.lightthemedemo.services.preferences.WidgetPrefs_;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidWidgetService implements LightWidgetService {

    @RootContext
    Context rootContext;

    @Pref
    WidgetPrefs_ widgetPrefs;

    @Override
    public int getWidgetsCount() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(rootContext);
        ComponentName componentName = new ComponentName( rootContext, WidgetProvider_.class);

        return appWidgetManager.getAppWidgetIds(componentName).length;
    }

    @Override
    public int getWidgetScreenOption() {
        return widgetPrefs.screenOption().get();
    }

    @Override
    public int getWidgetScreenMode() {
        return widgetPrefs.screenMode().getOr(WidgetScreenStatus.WIDGET_DAY_SCREEN);
    }

    @Override
    public void setWidgetScreenMode(int screenMode) {

        widgetPrefs.screenMode().put( screenMode );

        //its time to notify widgets..
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(rootContext);
        ComponentName componentName = new ComponentName( rootContext, WidgetProvider_.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(componentName);

        //do this only if there are widgets.
        if( widgetIds.length > 0 ){
            Intent intent = new Intent(rootContext, WidgetProvider_.class );
            intent.setAction( AppWidgetManager.ACTION_APPWIDGET_UPDATE );
            intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds );
            rootContext.sendBroadcast( intent );
        }
    }
}