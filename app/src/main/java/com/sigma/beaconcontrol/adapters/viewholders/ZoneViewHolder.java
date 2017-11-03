package com.sigma.beaconcontrol.adapters.viewholders;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.ZoneListAdapter;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneViewHolder extends ViewHolder {

    private AppCompatImageView zoneImage;
    private CustomTextView zoneName;
    private RadioButton zoneSelected;

    public ZoneViewHolder(View itemView) {
        super(itemView);

        zoneImage = itemView.findViewById(R.id.zone_image);
        zoneName = itemView.findViewById(R.id.zone_name);
        zoneSelected = itemView.findViewById(R.id.zone_selected);
    }

    public void bindElement(final ZoneListAdapter adapter, Zone zone,
                            String previousSelectedZoneName) {

        zoneImage.setImageBitmap(zone.getSelectedIcon());
        zoneName.setText(zone.getName());
        zoneSelected.setTag(zone);

        if (previousSelectedZoneName == null)
            zoneSelected.setVisibility(View.GONE);
        else if (previousSelectedZoneName.equals(zone.getName())) {
            zoneSelected.setChecked(true);
            if (adapter.getSelectedRadio() != null)
                adapter.getSelectedRadio().setChecked(false);

            adapter.setSelectedRadio(zoneSelected);
        }

        zoneSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (adapter.getSelectedRadio() != null)
                    adapter.getSelectedRadio().setChecked(false);

                adapter.setSelectedRadio(zoneSelected);
            }
        });

    }

}
