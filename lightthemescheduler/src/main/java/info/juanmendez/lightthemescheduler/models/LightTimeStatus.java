package info.juanmendez.lightthemescheduler.models;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class LightTimeStatus {

    public static final int SERVER_ERROR = 10000;
    public static final int NO_INTERNET = 10001;
    public static final int NO_LOCATION_PERMISSION = 10002;
    public static final int NO_LOCATION_AVAILABLE = 10003;
    public static final int LIGHTTIME_GUESSED = 10004;

    public static final int CANCEL_ANY_SCHEDULE = 2001;
    public static final int NEXT_SCHEDULE = 2002;
}
