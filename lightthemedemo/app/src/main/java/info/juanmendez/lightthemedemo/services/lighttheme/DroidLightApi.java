package info.juanmendez.lightthemedemo.services.lighttheme;

import android.location.Location;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import info.juanmendez.lightthemedemo.services.sunrise.LightTimeCalls;
import info.juanmendez.lightthemedemo.services.sunrise.LightTimeResponse;
import info.juanmendez.lightthemescheduler.models.LightTime;
import info.juanmendez.lightthemescheduler.models.LightTimeStatus;
import info.juanmendez.lightthemescheduler.models.Response;
import info.juanmendez.lightthemescheduler.services.LightApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@EBean
public class DroidLightApi implements LightApi {

    @Bean
    DroidLocationService locationService;

    Retrofit retrofit;
    LightTimeCalls lightTimeCalls;

    public DroidLightApi() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.sunrise-sunset.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        lightTimeCalls = retrofit.create(LightTimeCalls.class);
    }

    @Override
    public void generateTodayTimeLight(Response<LightTime> response) {
        makeCall("today", response);
    }

    @Override
    public void generateTomorrowTimeLight(Response<LightTime> response) {
        makeCall("tomorrow", response);
    }

    private void makeCall(String dateString, Response<LightTime> response) {

        Location location = locationService.getLastKnownLocation();

        if( location == null ){
            LightTime errorLightTime = new LightTime();
            errorLightTime.setStatus(LightTimeStatus.NO_LOCATION_AVAILABLE );
            response.onResult( errorLightTime );
            return;
        }

        Call<LightTimeResponse> call = lightTimeCalls.getLightTime(location.getLatitude(), location.getLongitude(), 0, dateString);

        call.enqueue(new Callback<LightTimeResponse>() {
            @Override
            public void onResponse(Call<LightTimeResponse> call, retrofit2.Response<LightTimeResponse> retrofitResponse) {
                LightTimeResponse sun = retrofitResponse.body();

                if( sun.getStatus() != null) {
                    if( sun.getStatus().equals("OK") ){
                        response.onResult(sun.getResults());
                    }else{
                        response.onResult(new LightTime());
                    }
                } else {
                    LightTime lightTime = new LightTime();
                    lightTime.setStatus( LightTimeStatus.NO_INTERNET );
                    response.onResult(lightTime);
                }
            }

            @Override
            public void onFailure(Call<LightTimeResponse> call, Throwable t) {
                response.onResult(new LightTime());
            }
        });
    }
}
