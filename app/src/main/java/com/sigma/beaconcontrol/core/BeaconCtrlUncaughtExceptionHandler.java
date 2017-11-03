package com.sigma.beaconcontrol.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sigma.beaconcontrol.exceptions.ExceptionHandler;

/**
 * Created by Wilson on 7/13/17.
 */

public class BeaconCtrlUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = BeaconCtrlUncaughtExceptionHandler.class.getSimpleName();

    private Thread.UncaughtExceptionHandler previousExceptionHandler;
    private Context context;

    public BeaconCtrlUncaughtExceptionHandler(Context context) {
        this.previousExceptionHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            chainExceptionHandler(thread, throwable);
            ExceptionHandler.handleException(throwable);
            System.exit(2);

        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void chainExceptionHandler(@Nullable Thread t, @NonNull Throwable e) {
        if (previousExceptionHandler != null) {
            previousExceptionHandler.uncaughtException(t, e);
            Log.i(TAG, "Chaining crash reporting duties to " + previousExceptionHandler.getClass().getSimpleName());
        } else {
            Log.e(TAG, "DynaTracker is disabled for " + context.getPackageName() +
                    " - no default ExceptionHandler. Caught a " + e.getClass().getSimpleName() +
                    " for " + context.getPackageName(), e);
        }
    }
}
