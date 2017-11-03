package info.juanmendez.widgetnightmodedemo.services.api.lighttheme;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.LocalTime;

import info.juanmendez.daynightthemescheduler.LightThemeClient;
import info.juanmendez.daynightthemescheduler.models.LightThemeModule;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean(scope = EBean.Scope.Singleton)
public class LightClientBuilder{

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
   SunriseSunsetApi sunriseSunsetApi;

   private LightThemeModule m;
   private LightThemeClient mClient;

   @AfterInject
   public void afterInject(){
       m = LightThemeModule.create()
               .applyLightTimeStorage( lightTimeStorage )
               .applyLighTimeApi( sunriseSunsetApi )
               .applyLocationService(  locationService )
               .applyNetworkService( networkService )
               .applyNow( LocalTime.now() );

       mClient = new LightThemeClient( m, widgetService, alarmService );
   }

   public LightThemeClient getClient(){
       return mClient;
   }
}