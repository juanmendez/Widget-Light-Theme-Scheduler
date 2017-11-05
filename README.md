# LightTheme-Scheduler
---------------------

An unopinionated library to set night-auto theme to widgets. It requires dependencies which are all defined by the library's interfaces, and such instances are included in `LightThemeModule`.
Once the module is created it is then assigned to `LightThemeManager`.

```java
module = LightThemeModule.create()
    //stores sunrise and sunset. could be in sharedPreferences
    .applyLightTimeStorage( lightTimeStorage )
    //calls webservice or implements a sunrise/sunset generator library
    .applyLightTimeApi( sunriseSunsetApi ) 
    //returns last known location, and if app has location permissions
    .applyLocationService(  locationService ) 
    //returns networkstate if online, but can be ignored if not using online access for sunrise/sunset
    .applyNetworkService( networkService ) 
    //provides number of widgets, and saves the widget theme for day or night
    .applyWidgetService( widgetService ) 
    //optional for testing, skip in production
    .applyTestableNow( LocalTime.parse("14:00")) 
    //covers scheduling the next sun event, also schedules a job when user is back online
    .applyAlarmService( alarmService ); 

mLightThemeManager = new LightThemeManager( module );
```

Your own `WidgetProvider` must pass its events to `LightThemeManager`. Also include a BroadcastReceiver to catch when the device turns on.

```java
    @Inject
    LightThemeManager mLightThemeManager;
    
     @Override
    public void onReceive(Context context, Intent intent) {
       mLightThemeManager.onAppEvent( intent.getAction() );
    }
```

```java
public class RebootReceiver extends BroadcastReceiver {

    @Inject
    LightThemeManager mLightThemeManager;

    @Override
    public void onReceive(Context context, Intent intent) {
       if( intent.getAction().equals(Intent.ACTION_REBOOT))
            mLightThemeManager.onAppEvent( intent.getAction() );
    }
}
```

`LightThemeManager` doesn't have to be a Singleton or be referenced using dependency injection, but works fine as well.
The library only requires [Joda Time](http://www.joda.org/joda-time/). Feel free to apply the libraries of your choice.

Please look into the [demo](/lightthemedemo) for more insights