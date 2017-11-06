package info.juanmendez.lightthemedemo.services.sunrise;

/**
 * Created by Juan Mendez on 11/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class Results
{
    private String day_length;

    private String sunset;

    private String nautical_twilight_begin;

    private String solar_noon;

    private String astronomical_twilight_end;

    private String civil_twilight_end;

    private String astronomical_twilight_begin;

    private String nautical_twilight_end;

    private String sunrise;

    private String civil_twilight_begin;

    public String getDay_length ()
    {
        return day_length;
    }

    public void setDay_length (String day_length)
    {
        this.day_length = day_length;
    }

    public String getSunset ()
    {
        return sunset;
    }

    public void setSunset (String sunset)
    {
        this.sunset = sunset;
    }

    public String getNautical_twilight_begin ()
    {
        return nautical_twilight_begin;
    }

    public void setNautical_twilight_begin (String nautical_twilight_begin)
    {
        this.nautical_twilight_begin = nautical_twilight_begin;
    }

    public String getSolar_noon ()
    {
        return solar_noon;
    }

    public void setSolar_noon (String solar_noon)
    {
        this.solar_noon = solar_noon;
    }

    public String getAstronomical_twilight_end ()
    {
        return astronomical_twilight_end;
    }

    public void setAstronomical_twilight_end (String astronomical_twilight_end)
    {
        this.astronomical_twilight_end = astronomical_twilight_end;
    }

    public String getCivil_twilight_end ()
    {
        return civil_twilight_end;
    }

    public void setCivil_twilight_end (String civil_twilight_end)
    {
        this.civil_twilight_end = civil_twilight_end;
    }

    public String getAstronomical_twilight_begin ()
    {
        return astronomical_twilight_begin;
    }

    public void setAstronomical_twilight_begin (String astronomical_twilight_begin)
    {
        this.astronomical_twilight_begin = astronomical_twilight_begin;
    }

    public String getNautical_twilight_end ()
    {
        return nautical_twilight_end;
    }

    public void setNautical_twilight_end (String nautical_twilight_end)
    {
        this.nautical_twilight_end = nautical_twilight_end;
    }

    public String getSunrise ()
    {
        return sunrise;
    }

    public void setSunrise (String sunrise)
    {
        this.sunrise = sunrise;
    }

    public String getCivil_twilight_begin ()
    {
        return civil_twilight_begin;
    }

    public void setCivil_twilight_begin (String civil_twilight_begin)
    {
        this.civil_twilight_begin = civil_twilight_begin;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [day_length = "+day_length+", sunset = "+sunset+", nautical_twilight_begin = "+nautical_twilight_begin+", solar_noon = "+solar_noon+", astronomical_twilight_end = "+astronomical_twilight_end+", civil_twilight_end = "+civil_twilight_end+", astronomical_twilight_begin = "+astronomical_twilight_begin+", nautical_twilight_end = "+nautical_twilight_end+", sunrise = "+sunrise+", civil_twilight_begin = "+civil_twilight_begin+"]";
    }
}