package info.juanmendez.daynightthemescheduler.twists;

import org.mockito.Mockito;

import info.juanmendez.daynightthemescheduler.models.LightTime;
import info.juanmendez.daynightthemescheduler.models.Response;
import info.juanmendez.daynightthemescheduler.services.LightApi;
import info.juanmendez.daynightthemescheduler.utils.LightTimeUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TwistApi implements Twist<LightApi> {

    private LightTime mToday = new LightTime();
    private LightTime mTomorrow = new LightTime();
    private LightApi apiRetro;

    @Override
    public LightApi asMocked() {

        if( apiRetro == null ){
            //chicago.. https://api.sunrise-sunset.org/json?lat=41.8500300&lng=-87.6500500&formatted=0
            apiRetro = mock( LightApi.class );

            doAnswer(invocation -> {
                Response<LightTime> response = invocation.getArgumentAt(0, Response.class);
                response.onResult(mToday);
                return null;
            }).when(apiRetro).generateTodayTimeLight(any(Response.class));

            doAnswer(invocation -> {
                Response<LightTime> response = invocation.getArgumentAt(0, Response.class);
                response.onResult(mTomorrow);
                return null;
            }).when(apiRetro).generateTomorrowTimeLight(any(Response.class));
        }
        return  apiRetro;
    }

    public LightTime getToday() {
        return mToday;
    }

    public void setToday(LightTime today) {
        LightTimeUtils.copy(today, this.mToday);
    }

    public LightTime getTomorrow() {
        return mTomorrow;
    }

    public void setTomorrow(LightTime tomorrow) {
        LightTimeUtils.copy(tomorrow, this.mTomorrow);
    }

    @Override
    public void reset() {
        Mockito.reset( apiRetro );
    }
}
