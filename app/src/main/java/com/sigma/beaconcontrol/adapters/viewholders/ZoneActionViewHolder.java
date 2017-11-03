package com.sigma.beaconcontrol.adapters.viewholders;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneActionViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView zoneImage;
    private CustomEditText zoneName;
    private AppCompatImageButton deleteZone;

    public ZoneActionViewHolder(View itemView) {
        super(itemView);

        zoneImage = itemView.findViewById(R.id.zone_image);
        zoneName = itemView.findViewById(R.id.zone_name);
        deleteZone = itemView.findViewById(R.id.delete_zone_button);
    }

    public void bindElement(final Zone zone, final View.OnClickListener clickListener) {
        zoneImage.setImageBitmap(zone.getSelectedIcon());
        zoneName.setText(zone.getName());
        deleteZone.setTag(zone);

        deleteZone.setOnClickListener(clickListener);

    }

}

