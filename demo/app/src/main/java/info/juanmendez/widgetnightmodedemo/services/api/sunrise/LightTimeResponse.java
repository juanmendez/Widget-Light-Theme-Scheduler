package info.juanmendez.widgetnightmodedemo.services.api.sunrise;
import info.juanmendez.daynightthemescheduler.models.LightTime;

/**
 * Created by Juan Mendez on 10/18/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class LightTimeResponse {

    private LightTime mResults;
    private String mStatus;

    public LightTime getResults ()
    {
        return mResults;
    }

    public void setResults (LightTime results)
    {
        mResults = results;
    }

    public String getStatus ()
    {
        return mStatus;
    }

    public void setStatus (String status)
    {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "LightThemeClientResponse{" +
                "results=" + mResults +
                ", status='" + mStatus + '\'' +
                '}';
    }
}
