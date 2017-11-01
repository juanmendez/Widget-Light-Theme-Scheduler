package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import info.juanmendez.daynightthemescheduler.models.WidgetScreenStatus;
import info.juanmendez.daynightthemescheduler.services.LightWidgetService;
import info.juanmendez.widgetnightmodedemo.WidgetPrefs_;
import info.juanmendez.widgetnightmodedemo.WidgetProvider_;

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
    public void updateWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(rootContext);
        ComponentName componentName = new ComponentName( rootContext, WidgetProvider_.class);

        int[] widgetIds = appWidgetManager.getAppWidgetIds(componentName);

        Intent intent = new Intent(rootContext, WidgetProvider_.class );
        intent.setAction( AppWidgetManager.ACTION_APPWIDGET_UPDATE );
        intent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds );
        rootContext.sendBroadcast( intent );
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
    }
}
