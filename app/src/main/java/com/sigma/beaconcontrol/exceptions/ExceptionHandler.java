package com.sigma.beaconcontrol.exceptions;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Wilson on 7/13/17.
 */

public class ExceptionHandler {
    public static void handleException(Throwable ex, String title, String message){
        ex.printStackTrace();
        FirebaseCrash.report(ex);
    }

    public static void handleException(Throwable ex){
        ex.printStackTrace();
        FirebaseCrash.report(ex);
    }
}
