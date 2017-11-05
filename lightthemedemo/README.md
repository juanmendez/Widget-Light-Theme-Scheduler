LightTheme Demo
----------------------------------------

This demo shows how to support the requirements of LightThemeScheduler library. 

Feel free to use the same dependencies or use the ones in your own project.
Here is a list of the libraries used for this demo.

* [AndroidJob](https://github.com/evernote/android-job) It schedules sunrise or sunset events. It also schedules an alarm when detected the device is offline, and requires to go online. See `DroidAlarmService`.
* [Android Annotations](http://androidannotations.org/) This library is simple and great to use, and shows how to include Dependency Injection when dealing with dependencies going into the LightThemeScheduler library. The library uses as module `LightThemeModule`.
* [api.sunrise-sunset.org](https://api.sunrise-sunset.org) This demo uses a webservice to get sunrise and sunset, but there are libraries out there which can figure it out, so for that just make your class implement `LightApi`.
* [Retrofit](https://square.github.io/retrofit/) Used in this demo to get data from api.sunrise-sunset.org

This demo uses SharedPreferences defined by AndroidAnnotations, and can be found at `LightTimePrefs` and `WidgetPrefs`. So if simply using `SharedPreferences` feel free to include your implementation.

The implementations of `LightLocationService` and `LightNetworkService` can be pretty much copied. This demo shows how to update `LightThemeModule` in its `MainActivity` when changing to day-only, night-only, or auto.
Also look into `ConfigActivity` which pops up prior to including any widget, but remains visible if user hasn't granted location permissions.