package info.juanmendez.lightthemedemo.services.lighttheme;

import android.content.Context;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import info.juanmendez.lightthemescheduler.LightThemeManager;
import info.juanmendez.lightthemescheduler.models.LightThemeModule;

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
               //.applyLightTimeApi( new TestableLightApi( "16:51" )
               .applyLightTimeApi( sunriseSunsetApi )
               .applyLocationService(  locationService )
               .applyNetworkService( networkService )
               .applyWidgetService( widgetService )
               .applyAlarmService( alarmService );

       mManager = new LightThemeManager( m );
   }

   public LightThemeManager getManager(){
       return mManager;
   }

   public static LightThemeManager getManager(Context context ){
       return LightManagerFactory_.getInstance_(context).getManager();
   }
}