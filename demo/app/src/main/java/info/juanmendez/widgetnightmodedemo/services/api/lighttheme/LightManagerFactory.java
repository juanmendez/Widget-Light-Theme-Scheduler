package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.LightThemeManager;
import info.juanmendez.daynightthemescheduler.models.LightThemeModule;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean(scope = EBean.Scope.Singleton)
public class LightManagerFactory {

    @Bean
    DroidAlarmService alarmService;

    @Bean
    DroidLightTimeStorage lightTimeStorage;

    @Bean
    DroidLocationService locationService;

    @Bean
    DroidNetworkService networkService;

    @Bean
    DroidWidgetService widgetService;

   @Bean
   DroidLightApi sunriseSunsetApi;

   private LightThemeModule m;
   private LightThemeManager mManager;

   @AfterInject
   public void afterInject(){
       m = LightThemeModule.create()
               .applyLightTimeStorage( lightTimeStorage )
               .applyLightTimeApi( sunriseSunsetApi )
               .applyLocationService(  locationService )
               .applyNetworkService( networkService )
               .applyWidgetService( widgetService )
               .applyAlarmService( alarmService )
               .applyTestableNow( LocalTime.parse("16:38") );

       mManager = new LightThemeManager( m );
   }

   public LightThemeManager getManager(){
       return mManager;
   }

   public static LightThemeManager getManager(Context context ){
       return LightManagerFactory_.getInstance_(context).getManager();
   }
}