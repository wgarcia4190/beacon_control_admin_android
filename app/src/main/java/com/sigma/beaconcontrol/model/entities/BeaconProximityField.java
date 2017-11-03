package com.sigma.beaconcontrol.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.sigma.beaconcontrol.exceptions.ExceptionHandler;

import org.json.JSONObject;

/**
 * Created by Wilson on 10/13/17.
 */

public class BeaconProximityField implements Entity {
    private long id;
    private long beaconId;
    private String name;
    private String value;

    public BeaconProximityField() {

    }

    public BeaconProximityField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public BeaconProximityField(Parcel in) {
        this.id = in.readLong();
        this.beaconId = in.readLong();
        this.name = in.readString();
        this.value = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(long beaconId) {
        this.beaconId = beaconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.beaconId = jsonObject.getLong("beacon_id");
            this.name = jsonObject.getString("name");
            this.value = jsonObject.getString("value");
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String toJson() {
        return null;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", this.name);
            jsonObject.put("value", this.value);
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.id);
        parcel.writeLong(this.beaconId);
        parcel.writeString(this.name);
        parcel.writeString(this.value);
    }

    public static final Parcelable.Creator<BeaconProximityField> CREATOR = new Parcelable.Creator<BeaconProximityField>() {

        @Override
        public BeaconProximityField createFromParcel(Parcel source) {
            return new BeaconProximityField(source);
        }

        @Override
        public BeaconProximityField[] newArray(int size) {
            return new BeaconProximityField[size];
        }
    };
}
