package com.sigma.beaconcontrol.views.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.beaconsdk.BeaconSDK;
import com.sigma.beaconcontrol.core.BeaconCtrlApplication;
import com.sigma.beaconcontrol.core.BeaconCtrlFullScreenActivity;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.general.Utils;
import com.sigma.beaconcontrol.views.login.LoginActivity;

/**
 * Created by Wilson on 10/7/17.
 */

public class SplashScreenActivity extends BeaconCtrlFullScreenActivity {

    private static final String TAG = "SplashScreen";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_LOCATION = 2;

    private boolean initialLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);

        BeaconCtrlApplication
                .setBeaconSDK(BeaconSDK.getInstance(this,
                        getString(R.string.client_id),
                        getString(R.string.client_secret),
                        getString(R.string.user_id)));

        if (Utils.isInternetAvailable(this)) {
            if (checkPlayServices()) {
                String[] permissionsNeeded = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                if (!Utils.arePermissionsGranted(this, permissionsNeeded)) {
                    ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_LOCATION);
                } else {
                    startMainOrLoginActivity();
                }
            } else {
                UIUtils.showAlertMessage(this, "Necesita tener Google Play " +
                        "Services instalado!", "Información", new Callback() {
                    @Override
                    public void execute(Object parameters) {
                        System.exit(0);
                    }
                });
            }
        } else
            UIUtils.showNoInternetDialog(this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void startMainOrLoginActivity() {
        startGoogleClient();
    }

    private void startGoogleClient() {
        if (Utils.isBLEAvailable(this)) {
            if (!Utils.isLocationEnabled(this)) {
                UIUtils.showGPSEnabledMessage(this);
            } else if (!Utils.isBluetoothEnabled(this)) {
                UIUtils.showBluetoothEnabledMessage(this);
            } else {
                int SPLASH_DISPLAY_LENGTH = 1500;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent data = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        UIUtils.startActivity(SplashScreenActivity.this, data);
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
        }else{
            Log.i(TAG, "This device is not supported.");
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            presenter.addActivity(this);
            startMainOrLoginActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!initialLoading) {
            startMainOrLoginActivity();
        }

        initialLoading = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
