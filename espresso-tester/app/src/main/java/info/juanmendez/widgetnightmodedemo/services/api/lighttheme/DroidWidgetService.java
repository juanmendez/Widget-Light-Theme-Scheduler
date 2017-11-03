package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.EBean;

import info.juanmendez.daynightthemescheduler.services.LightWidgetService;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidWidgetService implements LightWidgetService {

    private static int sWidgetCount;
    private int mScreenOption;
    private int mWidgetScreenMode;

    @Override
    public int getWidgetsCount() {
        return sWidgetCount;
    }

    @Override
    public int getWidgetScreenOption() {
        return mScreenOption;
    }

    @Override
    public int getWidgetScreenMode() {
        return mWidgetScreenMode;
    }

    @Override
    public void setWidgetScreenMode(int screenMode) {
        mWidgetScreenMode = screenMode;
    }

    public static int getWidgetCount() {
        return sWidgetCount;
    }

    public static void setWidgetCount(int widgetCount) {
        sWidgetCount = widgetCount;
    }
}