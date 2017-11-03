package com.sigma.beaconcontrol.views.zone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.ZoneActionAdapter;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.presenters.ZonePresenter;
import com.sigma.beaconcontrol.views.widgets.CustomButton;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneListActivity extends ZoneActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.zone_list)
    public RecyclerView zoneRecyclerView;
    @BindView(R.id.add_zone_button)
    public CustomButton addZoneButton;

    private ZoneActionAdapter zoneActionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list_activity);

        presenter = new ZonePresenter();
        setupPresenter();

        init();
        setupEvents();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.zones_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        zoneRecyclerView.setLayoutManager(new LinearLayoutManager(ZoneListActivity.this));

        if (zoneList.isEmpty()) {
            presenter.getZones(new ResponseCallback() {
                @Override
                public void onSuccess(Object data) {
                    zoneList.addAll((ArrayList<Zone>) data);
                    loadZoneData();
                }

                @Override
                public void onFailure(String message, String title) {
                    UIUtils.showAlertMessage(ZoneListActivity.this, message, title);
                }
            });
        } else
            loadZoneData();
    }

    private void setupEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addZoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showEditTextAlertMessage(ZoneListActivity.this,
                        getResources().getString(R.string.adding_zone_message),
                        getResources().getString(R.string.adding_zone_title),
                        getResources().getString(R.string.adding_zone_button),
                        getResources().getString(R.string.adding_zone_cancel),
                        new Callback() {
                            @Override
                            public void execute(Object object) {
                                super.execute(object);

                                String zoneName = object.toString();
                                if (zoneName.isEmpty()) {
                                    UIUtils.showAlertMessage(ZoneListActivity.this, "Please introduce a name for the zone", "Error");
                                    return;
                                }

                                final Zone zone = new Zone(ZoneListActivity.this);
                                zone.setName(zoneName);
                                zone.setBeaconsCount(0);
                                zone.addRandomColor();

                                presenter.addZone(zone, new ResponseCallback() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        zone.setId((Long) data);
                                        zoneList.add(zone);
                                        loadZoneData();
                                    }

                                    @Override
                                    public void onFailure(String message, String title) {
                                        UIUtils.showAlertMessage(ZoneListActivity.this, message, title);
                                    }
                                });


                            }
                        });
            }
        });

    }

    private void loadZoneData() {
        zoneActionAdapter = new ZoneActionAdapter(zoneList.subList(1, zoneList.size() - 1), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Zone zone = (Zone) view.getTag();

                UIUtils.showAlertMessage(ZoneListActivity.this,
                        getResources().getString(R.string.delete_zone_message),
                        getResources().getString(R.string.delete_zone_title),
                        getResources().getString(R.string.delete_zone_ok),
                        getResources().getString(R.string.delete_zone_cancel), 0,
                        new Callback() {
                            @Override
                            public void execute(Object object) {
                                super.execute(object);
                                presenter.deleteZone(zone.getId(), new ResponseCallback() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        zoneList.remove(zone);
                                        loadZoneData();
                                    }

                                    @Override
                                    public void onFailure(String message, String title) {
                                        UIUtils.showAlertMessage(ZoneListActivity.this, message, title);
                                    }
                                });
                            }
                        }, null);
            }
        });
        zoneRecyclerView.setAdapter(zoneActionAdapter);
    }
}
