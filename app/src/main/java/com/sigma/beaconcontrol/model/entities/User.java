package com.sigma.beaconcontrol.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.sigma.beaconcontrol.exceptions.ExceptionHandler;
import com.sigma.beaconcontrol.helpers.general.DateUtils;

import org.json.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

/**
 * Created by Wilson on 10/7/17.
 */

public class User implements Entity  {

    private long id;
    private String email;
    private String beaconUID;
    private long applicationId;
    private String token;
    private String cookies;

    public User() {
    }

    public User(Parcel in) {
        id = in.readLong();
        email = in.readString();
        beaconUID = in.readString();
        applicationId = in.readLong();
        token = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBeaconUID() {
        return beaconUID;
    }

    public void setBeaconUID(String beaconUID) {
        this.beaconUID = beaconUID;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        try{
            JSONObject data = new JSONObject(jsonObject.getString("data"));
            JSONObject user = new JSONObject(data.getString("user"));

            this.id = user.getLong("id");
            this.email = user.getString("email");
            this.beaconUID = user.getString("default_beacon_uuid");
            this.applicationId = data.getLong("application_id");
        }catch (Exception ex){
            ExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String toJson() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.email);
        dest.writeString(this.beaconUID);
        dest.writeLong(this.applicationId);
        dest.writeString(this.token);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public boolean isTokenValid(){
        if(token != null) {
            Jwt<Header, Claims> parsedToken = Jwts.parser()
                    .parseClaimsJwt(token.substring(0, (token.lastIndexOf('.') + 1)));

            return !token.isEmpty() && !DateUtils.isAfter(parsedToken.getBody().getExpiration());
        }
        return false;
    }
}
