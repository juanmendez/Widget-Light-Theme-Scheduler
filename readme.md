# Night-Mode Widget
---------------------

An agnostic library to apply night auto theme to widgets. It is done in this way to have options for what third party libraries to depend on to make Rest calls, schedule Alarms, etc.
It keeps the library light, and doesn't require more than just `Joda Time`, a Java library for date and time.

For example, one request is to store the sunrise, sunset, and next schedule times in `SharedPreferences` or a local Database. The dependency needed implements the following:

```java
interface LightTimeStorage {
    LightTime getLightTime();
    void saveLightTime(LightTime lightTime );
}
```

This is what LightTime class looks like
```java
class LightTime {
   //Date formatted strings
   String sunrise;
   String sunset;
   String nextSchedule;
}   
```

Thereafter, we make an instance of `LightThemeManager` and pass our own implementation of `LightTimeStorage`.
```java
new LightThemeManager( ...ourLightTimeStorage );
```

In the same procedure we provide other dependencies. You can make each time an instance of LightThemeManager or stored it as Singleton in your application. It doesn't matter.

Thereafter your `WidgetProvider@onReceive` make calls to notify of events happening.
```java
lightThemeManager.onEvent( intent.getAction() );
```
All dependencies required can be found on LightThemeManager constructor. There is also a demo project which shows how it uses the library.
This library intends to keep everything as light as possible and depend on your project's own dependencies.

