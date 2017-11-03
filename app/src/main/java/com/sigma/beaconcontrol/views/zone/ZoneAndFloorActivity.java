package com.sigma.beaconcontrol.views.zone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.ZoneListAdapter;
import com.sigma.beaconcontrol.helpers.general.Constants;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.general.Utils;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.presenters.ZonePresenter;
import com.sigma.beaconcontrol.views.widgets.CustomButton;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneAndFloorActivity extends ZoneActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.beacon_filter_floor)
    public Spinner beaconFloorFilter;
    @BindView(R.id.zone_list)
    public RecyclerView zoneRecyclerView;
    @BindView(R.id.select_zone_and_floor_button)
    public CustomButton selectZoneButton;

    private String floor;
    private String zoneName;
    private ZoneListAdapter zoneListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_and_floor_activity);

        presenter = new ZonePresenter();
        setupPresenter();

        floor = getIntent().getStringExtra("floor");
        zoneName = getIntent().getStringExtra("zone_name");

        init();
        setupEvents();
    }

    private void init() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.zones_and_floor_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Utils.fillSpinner(beaconFloorFilter, R.array.floors, ZoneAndFloorActivity.this);
        beaconFloorFilter.setSelection("N/A".equals(floor) ? 0 : Integer.parseInt(floor));
        zoneRecyclerView.setLayoutManager(new LinearLayoutManager(ZoneAndFloorActivity.this));

        if (zoneList.isEmpty()) {
            presenter.getZones(new ResponseCallback() {
                @Override
                public void onSuccess(Object data) {
                    zoneList.addAll((ArrayList<Zone>) data);
                    loadZoneData();
                }

                @Override
                public void onFailure(String message, String title) {
                    UIUtils.showAlertMessage(ZoneAndFloorActivity.this, message, title);
                }
            });
        } else
            loadZoneData();
    }

    private void setupEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Constants.CANCEL_RESULT);
                finish();
            }
        });

        selectZoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("floor", beaconFloorFilter.getSelectedItem().toString());
                data.putExtra("zone", (Parcelable)
                        zoneListAdapter.getSelectedRadio().getTag());

                setResult(Constants.SUCCESS_RESULT, data);
                finish();
            }
        });
    }

    private void loadZoneData() {
        zoneListAdapter = new ZoneListAdapter(zoneList, zoneName);
        zoneRecyclerView.setAdapter(zoneListAdapter);
    }


}
