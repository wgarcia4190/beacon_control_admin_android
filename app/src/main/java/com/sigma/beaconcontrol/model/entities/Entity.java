package com.sigma.beaconcontrol.model.entities;

import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Wilson on 10/7/17.
 */

public interface Entity extends Serializable, Parcelable {

    public void fromJSON(JSONObject jsonObject);
    public String toJson();
}
