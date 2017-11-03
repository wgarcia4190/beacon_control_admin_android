package com.sigma.beaconcontrol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.viewholders.ZoneViewHolder;
import com.sigma.beaconcontrol.model.entities.Zone;

import java.util.List;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneListAdapter extends RecyclerView.Adapter<ZoneViewHolder> {

    private List<Zone> data;
    private String selectedZoneName;
    private RadioButton selectedRadio;

    public ZoneListAdapter(List<Zone> data, String selectedZoneName) {
        this.data = data;
        this.selectedZoneName = selectedZoneName;
    }

    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_list_item, parent, false);
        return new ZoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {
        holder.bindElement(this, data.get(position), selectedZoneName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public RadioButton getSelectedRadio() {
        return selectedRadio;
    }

    public void setSelectedRadio(RadioButton selectedRadio) {
        this.selectedRadio = selectedRadio;
    }
}
