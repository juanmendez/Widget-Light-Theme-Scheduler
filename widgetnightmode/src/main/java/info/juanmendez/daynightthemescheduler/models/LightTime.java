package info.juanmendez.daynightthemescheduler.models;

import android.support.annotation.NonNull;

/**
 * Created by Juan Mendez on 10/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This pojo provides sunrise, sunset.
 * It optionally provides schedule, an any status assigned through the library process.
 *
 * @see LightTimeStatus
 */
public class LightTime {
    private String mSunrise = "";
    private String mSunset = "";
    private String mNextSchedule = "";
    private int mStatus;

    public LightTime() {
    }

    public LightTime(@NonNull String sunrise, @NonNull String sunset) {
        mSunrise = sunrise;
        mSunset = sunset;
    }

    public LightTime(String sunrise, String sunset, String nextSchedule) {
        this( sunrise, sunset );
        mNextSchedule = nextSchedule;
    }

    public String getSunrise() {
        return mSunrise;
    }

    public void setSunrise(String sunrise) {
        mSunrise = sunrise;
    }

    public String getSunset() {
        return mSunset;
    }

    public void setSunset(String sunset) {
        mSunset = sunset;
    }

    public String getNextSchedule() {
        return mNextSchedule;
    }

    public void setNextSchedule(String nextSchedule) {
        mNextSchedule = nextSchedule;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "LightTime{" +
                "sunrise='" + mSunrise + '\'' +
                ", sunset='" + mSunset + '\'' +
                ", nextSchedule='" + mNextSchedule + '\'' +
                '}';
    }
}
