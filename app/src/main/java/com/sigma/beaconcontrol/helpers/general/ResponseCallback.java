package com.sigma.beaconcontrol.helpers.general;

/**
 * Created by Wilson on 10/7/17.
 */

public interface ResponseCallback {

    void onSuccess(Object data);
    void onFailure(String message, String title);
}
