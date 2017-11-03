package com.sigma.beaconcontrol.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;

import java.util.List;

/**
 * Created by Wilson on 10/18/17.
 */

public class ZoneArrayAdapter extends ArrayAdapter<Zone> {

    private LayoutInflater inflater;
    private List<Zone> data;
    private int layout;

    public ZoneArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Zone> data) {
        super(context, resource, data);

        this.data = data;
        this.layout = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(layout, parent, false);

        CustomTextView zoneName = row.findViewById(R.id.zone_name);
        View zoneColorVIew = row.findViewById(R.id.zone_color_view);

        Zone zone = data.get(position);
        zoneName.setText(zone.getName());
        zoneColorVIew.setBackgroundColor(Color.parseColor(zone.getColor()));

        return row;
    }
}
