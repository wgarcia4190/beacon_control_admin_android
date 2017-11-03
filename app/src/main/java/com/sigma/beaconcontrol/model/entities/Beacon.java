package com.sigma.beaconcontrol.model.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.sigma.beaconcontrol.exceptions.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wilson on 10/8/17.
 */

public class Beacon implements Entity {

    private long id;
    private String floor;
    private double lat;
    private double lng;
    private String location;
    private String name;
    private String protocol;
    private String vendor;
    private String proximityUUID;
    private Zone zone;
    private List<BeaconProximityField> beaconProximityFields;

    private Context context;

    public Beacon() {
    }

    public Beacon(Context context) {
        this.context = context;
    }

    public Beacon(Parcel in) {
        id = in.readLong();
        floor = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        location = in.readString();
        name = in.readString();
        protocol = in.readString();
        vendor = in.readString();
        proximityUUID = in.readString();
        zone = in.readParcelable(Zone.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProximityUUID() {
        return proximityUUID;
    }

    public void setProximityUUID(String proximityUUID) {
        this.proximityUUID = proximityUUID;
    }

    public Zone getZone() {
        if (zone == null) {
            zone = new Zone(context);
        }
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<BeaconProximityField> getBeaconProximityFields() {
        if (beaconProximityFields == null) {
            beaconProximityFields = new ArrayList<>();
        }
        return beaconProximityFields;
    }

    public void setBeaconProximityFields(List<BeaconProximityField> beaconProximityFields) {
        this.beaconProximityFields = beaconProximityFields;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.floor = jsonObject.getString("floor");
            this.lat = jsonObject.getDouble("lat");
            this.lng = jsonObject.getDouble("lng");
            this.location = jsonObject.getString("location");
            this.name = jsonObject.getString("name");
            this.protocol = jsonObject.getString("protocol");
            this.vendor = jsonObject.getString("vendor");
            this.proximityUUID = jsonObject.getString("proximity_uuid");

            if (jsonObject.has("parents")) {
                JSONArray zones = jsonObject.getJSONObject("parents").getJSONArray("zones");
                getZone().fromJSON(zones.getJSONObject(0));
            }

            if (jsonObject.has("children")) {
                JSONArray beaconProximityFieldsArray = jsonObject.getJSONObject("children")
                        .getJSONArray("beaconproximityfields");

                for (int index = 0; index < beaconProximityFieldsArray.length(); index++) {
                    BeaconProximityField beaconProximityField = new BeaconProximityField();
                    beaconProximityField.fromJSON(beaconProximityFieldsArray.getJSONObject(index));
                    getBeaconProximityFields().add(beaconProximityField);
                }
            }

            if ("0".equals(this.floor)) {
                this.floor = "N/A";
            }
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String toJson() {
        JSONObject beaconJson = new JSONObject();
        JSONObject beaconConfigJson = new JSONObject();
        JSONArray beaconProximityFieldsArray = new JSONArray();
        try {
            for(BeaconProximityField beaconProximityField : this.beaconProximityFields){
                beaconProximityFieldsArray.put(beaconProximityField.toJsonObject());
            }

            beaconConfigJson.put("status", "Active");
            beaconConfigJson.put("device_id", "Unknown");
            beaconConfigJson.put("vendor", this.vendor);
            beaconConfigJson.put("firmware", "Unknown");
            beaconConfigJson.put("battery", "Unknown");
            beaconConfigJson.put("last_action", new Date().toString());
            beaconConfigJson.put("average_connection_intervals", "Unknown");

            beaconJson.put("name", this.name);
            beaconJson.put("lat", this.lat);
            beaconJson.put("lng", this.lng);
            beaconJson.put("protocol", this.protocol);
            beaconJson.put("proximity_uuid", this.proximityUUID);
            beaconJson.put("vendor", this.vendor);
            beaconJson.put("floor", this.floor);
            beaconJson.put("proximity_fields", beaconProximityFieldsArray);
            beaconJson.put("beacon_config", beaconConfigJson);

            if (this.zone != null && this.zone.getId() != 0)
                beaconJson.put("zone_id", this.zone.getId());

            if(this.id != 0)
                beaconJson.put("id", this.id);

        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }

        return beaconJson.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.floor);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.location);
        dest.writeString(this.name);
        dest.writeString(this.protocol);
        dest.writeString(this.vendor);
        dest.writeString(this.proximityUUID);
        dest.writeParcelable(this.zone, flags);
    }

    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {

        @Override
        public Beacon createFromParcel(Parcel source) {
            return new Beacon(source);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    public String getProximityFieldValueByName(@NonNull String name) {
        if (!getBeaconProximityFields().isEmpty()) {
            for (BeaconProximityField proximityField : getBeaconProximityFields()) {
                if (name.equals(proximityField.getName()))
                    return proximityField.getValue();
            }
        }
        return null;
    }
}
