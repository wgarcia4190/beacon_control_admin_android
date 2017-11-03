package com.sigma.beaconcontrol.helpers.general;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.crash.FirebaseCrash;
import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.exceptions.ExceptionHandler;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wilson on 7/13/17.
 */

public class Utils {

    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
        return false;
    }

    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean arePermissionsGranted(@NonNull Context context, @NonNull String... permissions) {
        boolean ret = true;
        for (String perm : permissions) {
            if (!isPermissionGranted(context, perm)) {
                ret = false;
            }
        }
        return ret;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;


        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    public static boolean isBLEAvailable(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        return true;
    }

    public static boolean isBluetoothEnabled(Context context) {
        if (!((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter().isEnabled()) {
            return false;
        }

        return true;
    }

    public static void fillSpinner(Spinner spinner, int array, Context context) {
        List<String> values = Arrays.asList(context.getResources().getStringArray(array));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, values);
        spinner.setAdapter(adapter);
    }

    public static int getIndexOfArray(Context context, int array, Object value) {
        return Arrays.asList(context.getResources().getStringArray(array)).indexOf(value);
    }
}
