package com.sigma.beaconcontrol.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.sigma.beaconcontrol.BuildConfig;
import com.sigma.beaconcontrol.beaconsdk.BeaconSDK;
import com.sigma.beaconcontrol.beaconsdk.core.BeaconSDKDelegate;
import com.sigma.beaconcontrol.beaconsdk.core.model.Action;
import com.sigma.beaconcontrol.beaconsdk.core.model.Beacon;
import com.sigma.beaconcontrol.exceptions.ExceptionHandler;
import com.sigma.beaconcontrol.helpers.service.HttpScheme;
import com.sigma.beaconcontrol.model.http.services.rest.RestHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by Wilson on 7/13/17.
 */

public class BeaconCtrlApplication extends Application {

    private static final String TAG = BeaconCtrlApplication.class.getName();

    private static SharedPreferences sharedPreferences;
    private static RestHandler httpClient;
    private static BeaconSDK beaconSDK;

    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        httpClient = new RestHandler.Builder()
                .setBaseUrl(BuildConfig.API_PATH)
                .setScheme(HttpScheme.HTTP).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Thread.setDefaultUncaughtExceptionHandler(new BeaconCtrlUncaughtExceptionHandler(base));
    }

    private void printHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager()
                    .getPackageInfo("com.sigma.beaconcontrol", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public static SharedPreferences getPreferences() {
        return sharedPreferences;
    }

    public static RestHandler getHttpClient() {
        return httpClient;
    }

    public static BeaconSDK getBeaconSDK() {
        return beaconSDK;
    }

    public static void setBeaconSDK(BeaconSDK beaconSDK){
        BeaconCtrlApplication.beaconSDK = beaconSDK;

        BeaconCtrlApplication.beaconSDK.enableLogging(true);
        BeaconCtrlApplication.beaconSDK.setBeaconErrorListener(errorCode -> {
            Log.e(TAG, "Some error occured in Beacon SDK: " + errorCode.name());
        });

        BeaconCtrlApplication.beaconSDK.setBeaconDelegate(new BeaconSDKDelegate() {
            @Override
            public boolean shouldPerformActionAutomatically() {
                return true;
            }

            @Override
            public void onActionStart(Action action) {
                Log.d(TAG, "onActionStart");
            }

            @Override
            public void onActionEnd(Action action) {
                Log.d(TAG, "onActionEnd");
            }

            @Override
            public void onBeaconsConfigurationLoaded(List<Beacon> list) {
                Log.d(TAG, "onBeaconsConfigurationLoaded");
            }

            @Override
            public void onBeaconProximityChanged(Beacon beacon) {
                Log.d(TAG, "onBeaconProximityChanged");
            }
        });
    }
}
