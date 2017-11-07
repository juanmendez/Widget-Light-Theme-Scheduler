# LightTheme-Scheduler

This library has the logic to schedule your widgets for their day or night themes. This library is agnostic about what other libraries are used in your application; although, it requires implementations to interact with your dependencies.
Each of those implementation interfaces can be found at `LightThemeModule`. Once the module is created it is then assigned to `LightThemeManager`.

```java
module = LightThemeModule.create()
    //stores sunrise and sunset. SharedPreferences can be used for it
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
    //covers scheduling for the next sun event. It also schedules a job if device was found offline and needs to pull data
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

The library makes use of [Joda Time](http://www.joda.org/joda-time/). Feel free to apply the libraries of your choice.

Please look into the [demo](/lightthemedemo) for more insights
