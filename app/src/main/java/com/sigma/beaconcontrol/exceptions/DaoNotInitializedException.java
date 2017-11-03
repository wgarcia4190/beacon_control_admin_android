package com.sigma.beaconcontrol.exceptions;

/**
 * Created by Wilson on 10/7/17.
 */

public class DaoNotInitializedException extends RuntimeException {

    public DaoNotInitializedException() {
        super("Dao needs to be initialized!!!!");
    }

    public DaoNotInitializedException(String s) {
        super(s);
    }

    public DaoNotInitializedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DaoNotInitializedException(Throwable throwable) {
        super(throwable);
    }
}
