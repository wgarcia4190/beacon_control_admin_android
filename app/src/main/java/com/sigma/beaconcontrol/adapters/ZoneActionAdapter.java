package com.sigma.beaconcontrol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.viewholders.ZoneActionViewHolder;
import com.sigma.beaconcontrol.model.entities.Zone;

import java.util.List;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneActionAdapter extends RecyclerView.Adapter<ZoneActionViewHolder> {

    private List<Zone> data;
    private View.OnClickListener clickListener;

    public ZoneActionAdapter(List<Zone> data, View.OnClickListener clickListener) {
        this.data = data;
        this.clickListener = clickListener;
    }

    @Override
    public ZoneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_action_list_item, parent, false);
        return new ZoneActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ZoneActionViewHolder holder, int position) {
        holder.bindElement(data.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
