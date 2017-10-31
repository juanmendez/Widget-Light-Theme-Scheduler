package info.juanmendez.widgetnightmodedemo.services;

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
@EBean
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

   public LightThemeClient create(){
       LightThemeModule m = LightThemeModule.create()
               .applyLightTime( lightTimeStorage.getLightTime() )
               .applyLighTimeApi( sunriseSunsetApi )
               .applyLocationService(  locationService )
               .applyNetworkService( networkService )
               .applyNow( LocalTime.now() );

       return  new LightThemeClient( m, widgetService, alarmService, lightTimeStorage );
   }
}
