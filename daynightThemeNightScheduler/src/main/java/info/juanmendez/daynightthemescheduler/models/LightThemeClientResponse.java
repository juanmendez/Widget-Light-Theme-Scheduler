package info.juanmendez.daynightthemescheduler.models;

/**
 * Created by Juan Mendez on 10/30/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class LightThemeClientResponse {
    private int status;
    private LightTime lightTime = new LightTime();

    public static LightThemeClientResponse create( int status ){
        LightThemeClientResponse response = new LightThemeClientResponse();
        response.status = status;
        return response;
    }

    public static LightThemeClientResponse create( int status, LightTime lightTime ){
        LightThemeClientResponse response = new LightThemeClientResponse();
        response.status = status;
        response.lightTime = lightTime;
        return response;
    }

    public int getStatus() {
        return status;
    }

    public LightTime getLightTime() {
        return lightTime;
    }
}
