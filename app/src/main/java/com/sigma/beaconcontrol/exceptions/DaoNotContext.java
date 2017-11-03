package com.sigma.beaconcontrol.exceptions;

/**
 * Created by Wilson on 10/7/17.
 */

public class DaoNotContext extends RuntimeException {

    public DaoNotContext() {
        super("Context not set in DAO!!!");
    }

    public DaoNotContext(String s) {
        super(s);
    }

    public DaoNotContext(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DaoNotContext(Throwable throwable) {
        super(throwable);
    }
}
