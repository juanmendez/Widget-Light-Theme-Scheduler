# LightTheme-Scheduler
---------------------

An agnostic library to apply night auto theme to widgets. It requires dependencies which are all defined by interfaces, and such instances are included in `LightThemeModule`.
Once the module is created it is then assigned to `LightThemeManager`.

```java
module = LightThemeModule.create()
               .applyLightTimeStorage( lightTimeStorage ) //stores sunrise and sunset. could be in sharedPreferences
               .applyLightTimeApi( sunriseSunsetApi ) //calls webservice or implements a sunrise/sunset generator library
               .applyLocationService(  locationService ) //returns last known location, and if app has location permissions
               .applyNetworkService( networkService ) //returns networkstate if online, but can be ignored if not using online access for sunrise/sunset
               .applyWidgetService( widgetService ) //provides number of widgets, and saves the widget theme for day or night
               .applyTestableNow( LocalTime.parse("14:00")) //optional for testing, skip in production
               .applyAlarmService( alarmService ); //covers scheduling the next sun event, also schedules a job when user is back online

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
