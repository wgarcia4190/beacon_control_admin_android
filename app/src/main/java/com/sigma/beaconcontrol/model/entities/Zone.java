package com.sigma.beaconcontrol.model.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.exceptions.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

/**
 * Created by Wilson on 10/8/17.
 */

public class Zone implements Entity {

    private long id;
    private int accountId;
    private int beaconsCount;
    private String color = "#494846";
    private String description;
    private int managerId;
    private String name = "Unassigned";
    private Bitmap unselectedIcon;
    private Bitmap selectedIcon;

    private int imageSize = 68;
    private Context context;
    private String[] colorArray = new String[]{
            "#05796f", "#19a69a", "#63bc66", "#9bcd5f", "#ffa800", "#ffcb00", "#fff048",
            "#d4e34a", "#ff6f3a", "#f2514b", "#ef3c79", "#ac41be", "#7e53c5", "#5b69c3", "#3b4bb2",
            "#222c6f"
    };

    public Zone() {
    }

    public Zone(Context context) {
        this.context = context;
    }

    public Zone(Parcel in) {
        id = in.readLong();
        accountId = in.readInt();
        beaconsCount = in.readInt();
        color = in.readString();
        description = in.readString();
        managerId = in.readInt();
        name = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBeaconsCount() {
        return beaconsCount;
    }

    public void setBeaconsCount(int beaconsCount) {
        this.beaconsCount = beaconsCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getUnselectedIcon() {
        if (unselectedIcon == null) {
            setZoneIcons(this.color);
        }
        return unselectedIcon;
    }

    public void setUnselectedIcon(Bitmap unselectedIcon) {
        this.unselectedIcon = unselectedIcon;
    }

    public Bitmap getSelectedIcon() {
        if (selectedIcon == null) {
            setZoneIcons(this.color);
        }
        return selectedIcon;
    }

    public void setSelectedIcon(Bitmap selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    @Override
    public void fromJSON(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.accountId = jsonObject.getInt("account_id");
            this.beaconsCount = jsonObject.getInt("beacons_count");
            this.color = jsonObject.getString("color");
            this.description = jsonObject.getString("description");
            this.managerId = jsonObject.getInt("manager_id");
            this.name = jsonObject.getString("name");

            if (!this.color.contains("#")) {
                String[] rgbColor = this.color.replace("rgb(", "").replace(")", "").split(",");
                this.color = String.format("#%02x%02x%02x", Integer.parseInt(rgbColor[0].trim()),
                        Integer.parseInt(rgbColor[1].trim()), Integer.parseInt(rgbColor[2].trim()));

                setZoneIcons(this.color);
            }
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    @Override
    public String toJson() {
        JSONObject zoneObject = new JSONObject();
        try {
            int color = (int)Long.parseLong(this.color.replace("#", ""), 16);
            String colorRgb = String.format(Locale.US, "rgb(%d, %d, %d)",
                    (color >> 16) & 0xFF, (color >> 8) & 0xFF, (color) & 0xFF);

            zoneObject.put("name", this.name);
            zoneObject.put("color", colorRgb);
            zoneObject.put("beacons_count", String.valueOf(this.beaconsCount));
            zoneObject.put("description", this.description);
            zoneObject.put("beacons", "[]");


            if (this.id != 0)
                zoneObject.put("id", id);

        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
        return zoneObject.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(this.id);
        dest.writeInt(this.accountId);
        dest.writeInt(this.beaconsCount);
        dest.writeString(this.color);
        dest.writeString(this.description);
        dest.writeInt(this.managerId);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<Zone> CREATOR = new Parcelable.Creator<Zone>() {

        @Override
        public Zone createFromParcel(Parcel source) {
            return new Zone(source);
        }

        @Override
        public Zone[] newArray(int size) {
            return new Zone[size];
        }
    };

    private void setZoneIcons(String color) {
        switch (color) {
            case "#05796f":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_1, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_1, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#19a69a":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_2, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_2, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#63bc66":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_3, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_3, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#9bcd5f":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_4, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_4, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#ffa800":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_5, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_5, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#ffcb00":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_6, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_6, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#fff048":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_7, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_7, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#d4e34a":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_8, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_8, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#ff6f3a":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_9, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_9, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#f2514b":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_10, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_10, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#ef3c79":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_11, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_11, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#ac41be":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_12, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_12, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#7e53c5":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_13, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = this.unselectedIcon = this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_13, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#5b69c3":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_14, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_14, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#3b4bb2":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_15, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_15, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            case "#222c6f":
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unselected_16, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.selected_16, null))
                        .getBitmap(), imageSize, imageSize, false);
                break;
            default:
                this.unselectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unassigned_unselected, null))
                        .getBitmap(), imageSize, imageSize, false);
                this.selectedIcon = Bitmap.createScaledBitmap(((BitmapDrawable) context.getResources()
                        .getDrawable(R.drawable.unassigned_selected, null))
                        .getBitmap(), imageSize, imageSize, false);
        }
    }

    public void addRandomColor() {
        this.setColor(colorArray[new Random().nextInt(colorArray.length)]);
    }
}
