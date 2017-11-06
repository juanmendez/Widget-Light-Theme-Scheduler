package info.juanmendez.lightthemedemo.services.lighttheme;

import android.location.Location;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import info.juanmendez.lightthemedemo.services.sunrise.LightTimeCalls;
import info.juanmendez.lightthemedemo.services.sunrise.Results;
import info.juanmendez.lightthemedemo.services.sunrise.SunriseSunsetContent;
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

        Location location = locationService.getLocation();
        Call<SunriseSunsetContent> call = lightTimeCalls.getLightTime(location.getLatitude(), location.getLongitude(), 0, dateString);

        call.enqueue(new Callback<SunriseSunsetContent>() {
            @Override
            public void onResponse(Call<SunriseSunsetContent> call, retrofit2.Response<SunriseSunsetContent> retrofitResponse) {
                SunriseSunsetContent content = retrofitResponse.body();

                if( content.getStatus() != null && content.getStatus().equals("OK") ) {
                    Results results = content.getResults();

                    //Sunrise/Sunset times must be in UTC
                    LightTime lightTime = new LightTime( results.getSunrise(), results.getSunset() );
                    response.onResult(lightTime);
                } else {
                    LightTime lightTime = new LightTime();
                    lightTime.setStatus( LightTimeStatus.SERVER_ERROR);
                    response.onResult(lightTime);
                }
            }

            @Override
            public void onFailure(Call<SunriseSunsetContent> call, Throwable t) {
                LightTime lightTime = new LightTime();
                lightTime.setStatus( LightTimeStatus.SERVER_ERROR);
                response.onResult(lightTime);
            }
        });
    }
}
