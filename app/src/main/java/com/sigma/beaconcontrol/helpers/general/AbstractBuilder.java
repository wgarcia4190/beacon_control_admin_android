package com.sigma.beaconcontrol.helpers.general;

/**
 * Created by Wilson on 7/13/17.
 */

public abstract class AbstractBuilder<T> {
    public T built;

    public abstract T createInstance();

    public AbstractBuilder() {
        this.built = null;
    }

    public T build() {
        init();
        return this.built;
    }

    public void init() {
        if (this.built == null) {
            this.built = createInstance();
        }
    }
}
