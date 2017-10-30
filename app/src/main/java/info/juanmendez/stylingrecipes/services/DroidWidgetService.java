package info.juanmendez.stylingrecipes.services;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import info.juanmendez.daynightthemescheduler.services.LightWidgetService;
import info.juanmendez.stylingrecipes.WidgetProvider_;


/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidWidgetService implements LightWidgetService {

    @RootContext
    Context rootContext;

    @Override
    public int getObserversCount() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(rootContext);
        ComponentName componentName = new ComponentName( rootContext, WidgetProvider_.class);

        return appWidgetManager.getAppWidgetIds(componentName).length;
    }

    @Override
    public void updateLightTheme(int theme) {

    }
}
