package info.juanmendez.lighttheme;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import info.juanmendez.lighttheme.models.LightThemeModule;
import info.juanmendez.lighttheme.models.LightTime;
import info.juanmendez.lighttheme.models.LightTimeStatus;
import info.juanmendez.lighttheme.models.WidgetScreenStatus;
import info.juanmendez.lighttheme.services.LightPlanner;
import info.juanmendez.lighttheme.utils.LightTimeUtils;

import static info.juanmendez.lighttheme.utils.LightTimeUtils.getScreenMode;


/**
 * Created by Juan Mendez on 10/29/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class LightThemeManager {

    private static final String CLASS_NAME = LightThemeManager.class.getName();
    public static final String THEME_OPTION_CHANGED = CLASS_NAME + ".THEME_OPTION_CHANGED";
    public static final String ALARM_EXECUTED = CLASS_NAME + ".ALARM_EXECUTED";
    public static final String ALARM_EXECUTED_ONLINE = CLASS_NAME + ".ALARM_EXECUTED_ONLINE";

    private LightThemeModule m;
    private LightPlanner mPlanner;

    public LightThemeManager(LightThemeModule module ) {
        m = module;
        mPlanner = new LightPlanner(m);
    }

    /**
     * widget added/removed. device reboot
     */
    public void onAppEvent(@NonNull String appEvent ){

        if(appEvent.equals(THEME_OPTION_CHANGED)){

            if( m.getWidgetService().getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){
                planNextSchedule();
            }else{
                reflectScreenMode();
                m.getAlarmService().cancelIfRunning();
            }
        }else if( appEvent.equals( AppWidgetManager.ACTION_APPWIDGET_ENABLED) ){
            if( m.getWidgetService().getWidgetsCount() == 1 ){
                /*m.applyTestableNow( LocalTime.parse("16:38"));*/
                planNextSchedule();
            }
        }else if( appEvent.equals( AppWidgetManager.ACTION_APPWIDGET_DELETED) ){
            if( m.getWidgetService().getWidgetsCount() == 0 ){
                m.getAlarmService().cancelIfRunning();
            }
        }else if( appEvent.equals(Intent.ACTION_REBOOT)){
           /* m.applyTestableNow( LocalTime.parse("16:41"));*/
            planNextSchedule();
        }else if( appEvent.equals(ALARM_EXECUTED)){
           /* m.applyTestableNow( LocalTime.parse("16:41"));*/
            planNextSchedule();
        }else if( appEvent.equals(ALARM_EXECUTED_ONLINE)){
            /*m.applyTestableNow( LocalTime.parse("16:41"));*/
            planNextSchedule( );
        }
    }

    /**
     * There hasn't been any schedule done.
     * This is the case of device reboot, or first time adding a widget
     * We will attempt to start
     */
    public void onScheduleRequest(){
        //We want to enforce lightTime in module has no schedule
        m.getLightTime().setNextSchedule("");
        planNextSchedule( );
    }

    private void  planNextSchedule(){

        if( m.getWidgetService().getWidgetsCount() > 0 && m.getWidgetService().getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO ){

            mPlanner.generateLightTime(lightTimeResult -> {

                m.getLightTimeStorage().saveLightTime( lightTimeResult );

                if( !lightTimeResult.getNextSchedule().isEmpty() ){

                    /**
                     * This library found evidence setting the schedule at the expected time can have some drawbacks.
                     * For preventing miscalculations, lets increase the scheduled time by 30 seconds only.
                     */
                    Long msFromNow = LightTimeUtils.getMSFromSchedule( m.getNow(), lightTimeResult);
                    msFromNow += 30_000L;

                    m.getAlarmService().scheduleNext( msFromNow );
                }else if( lightTimeResult.getStatus() == LightTimeStatus.NO_INTERNET ){

                    lightTimeResult.setNextSchedule("");
                    m.getAlarmService().scheduleNextWhenOnline();
                }else{
                    m.getAlarmService().cancelIfRunning();
                }

                reflectScreenMode();
            });
        }else{
            m.getAlarmService().cancelIfRunning();
            reflectScreenMode();
        }
    }

    public void reflectScreenMode(){

        LightTime todayLightTime = mPlanner.getTodayLightTime();

        int nextScreenMode = m.getWidgetService().getWidgetScreenMode();

        if( m.getWidgetService().getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_AUTO && LightTimeUtils.isValid( todayLightTime ) ){
            nextScreenMode = getScreenMode( m.getNow(), todayLightTime );
        }else if( m.getWidgetService().getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_YES ){
            nextScreenMode = WidgetScreenStatus.WIDGET_NIGHT_SCREEN;
        }else if( m.getWidgetService().getWidgetScreenOption() == AppCompatDelegate.MODE_NIGHT_NO ) {
            nextScreenMode = WidgetScreenStatus.WIDGET_DAY_SCREEN;
        }

        if( nextScreenMode != m.getWidgetService().getWidgetScreenMode() ){
            m.getWidgetService().setWidgetScreenMode( nextScreenMode );
        }
    }
}