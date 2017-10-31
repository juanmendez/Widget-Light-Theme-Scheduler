package info.juanmendez.daynightthemescheduler.twists;

/**
 * Created by Juan Mendez on 10/31/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface Twist<T> {
    T asMocked();
    void reset();
}
