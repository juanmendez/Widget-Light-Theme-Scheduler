package info.juanmendez.lightthemedemo.services.sunrise;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Juan Mendez on 10/18/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public interface LightTimeCalls{

    @GET("/json")
    Call<SunriseSunsetContent> getLightTime(@Query("lat") double lat, @Query("lng") double lng, @Query("formatted") int formatted, @Query("date") String date);
}
