package info.juanmendez.lightthemedemo.services.sunrise;
/**
 * Created by Juan Mendez on 10/18/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class SunriseSunsetContent
{
    private Results results;

    private String status;

    public Results getResults ()
    {
        return results;
    }

    public void setResults (Results results)
    {
        this.results = results;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+", status = "+status+"]";
    }
}
