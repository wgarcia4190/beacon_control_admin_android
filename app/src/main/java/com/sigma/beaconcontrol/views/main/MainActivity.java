package com.sigma.beaconcontrol.views.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.adapters.ZoneArrayAdapter;
import com.sigma.beaconcontrol.core.BeaconCtrlActivity;
import com.sigma.beaconcontrol.core.BeaconCtrlApplication;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.Constants;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.general.Utils;
import com.sigma.beaconcontrol.helpers.map.MapHelper;
import com.sigma.beaconcontrol.helpers.map.MapTouchEventListener;
import com.sigma.beaconcontrol.helpers.map.MarkerFilterEnum;
import com.sigma.beaconcontrol.model.entities.Beacon;
import com.sigma.beaconcontrol.model.entities.BeaconProximityField;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.presenters.MainPresenter;
import com.sigma.beaconcontrol.views.login.LoginActivity;
import com.sigma.beaconcontrol.views.notification.NotificationActivity;
import com.sigma.beaconcontrol.views.widgets.CustomButton;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;
import com.sigma.beaconcontrol.views.widgets.CustomMapFragment;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;
import com.sigma.beaconcontrol.views.zone.ZoneAndFloorActivity;
import com.sigma.beaconcontrol.views.zone.ZoneListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BeaconCtrlActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener, MapTouchEventListener {

    private static final int REQUEST_LOCATION = 2;
    private static final int NOTIFICATION_LOCATION = 1001;
    private static final int ZONE_AND_FLOOR_LOCATION = 2001;

    private ActionBarDrawerToggle toggle;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker previousSelectedMarker;
    private List<Marker> beaconMarkers;
    private Intent customAttributes;
    private String[] permissionsNeeded = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LongSparseArray<Zone> zonesById = new LongSparseArray<>();
    private CustomMapFragment mapFragment;
    private boolean isDraggingMap;
    private Zone selectedZone;
    private String selectedFloor;

    @BindView(R.id.map_beacon_pin)
    public AppCompatImageView mapNewBeaconPin;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.selected_zone_image)
    public AppCompatImageView selectedZoneImage;
    @BindView(R.id.zone_name)
    public CustomTextView zoneName;
    @BindView(R.id.floor_number)
    public CustomTextView floorNumber;
    @BindView(R.id.beacon_name)
    public CustomEditText beaconName;
    @BindView(R.id.cancel_button)
    public AppCompatImageButton cancelButton;
    @BindView(R.id.action_button)
    public AppCompatImageButton actionButton;
    @BindView(R.id.zone_data_container)
    public LinearLayout zoneDataContainer;
    @BindView(R.id.beacon_name_container)
    public LinearLayout beaconNameContainer;
    @BindView(R.id.beacon_details_container)
    public LinearLayout beaconDetailsContainer;
    @BindView(R.id.beacon_minor)
    public CustomEditText beaconMinor;
    @BindView(R.id.beacon_major)
    public CustomEditText beaconMajor;
    @BindView(R.id.beacon_lat)
    public CustomEditText beaconLat;
    @BindView(R.id.beacon_lng)
    public CustomEditText beaconLng;
    @BindView(R.id.beacon_uuid)
    public CustomEditText beaconUUID;
    @BindView(R.id.beacon_protocol)
    public Spinner beaconProtocol;
    @BindView(R.id.beacon_vendor)
    public Spinner beaconVendor;
    @BindView(R.id.ibeacon_proximity_fields_container)
    public LinearLayout iBeaconProximityFieldsContainer;
    @BindView(R.id.edddystone_proximity_fields_container)
    public LinearLayout eddystoneProximityFieldsContainer;
    @BindView(R.id.beacon_signal_interval)
    public CustomEditText beaconSignalInterval;
    @BindView(R.id.beacon_namespace)
    public CustomEditText beaconNamespace;
    @BindView(R.id.beacon_instance_id)
    public CustomEditText beaconInstanceId;
    @BindView(R.id.beacon_zone_image)
    public AppCompatImageView beaconZoneImage;
    @BindView(R.id.beacon_zone_name)
    public CustomTextView beaconZoneName;
    @BindView(R.id.beacon_floor_number)
    public CustomTextView beaconFloorNumber;
    @BindView(R.id.notification_container)
    public LinearLayout notificationContainer;
    @BindView(R.id.save_beacon_button)
    public CustomButton saveBeaconButton;
    @BindView(R.id.zone_and_floor_container)
    public LinearLayout zoneFloorContainer;
    @BindView(R.id.beacon_zone_dropdown)
    public Spinner beaconZoneDropdown;
    @BindView(R.id.beacon_floor_dropdown)
    public Spinner beaconFloorDropdown;
    @BindView(R.id.beacon_zone_data_container)
    public LinearLayout beaconZoneDataContainer;
    @BindView(R.id.zones_list_container)
    public LinearLayout zoneListContainer;

    /*Filter components*/
    private Spinner beaconFloorFilter;
    private LinearLayout toggleZoneContainer;
    private MenuItem deleteBeaconItem;
    private MenuItem addBeaconItem;
    /*Filter components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter();
        setupPresenter();

        BeaconCtrlApplication.getBeaconSDK().startScan(this);

        init();
    }

    private void init() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.main_activity_title);

        Utils.fillSpinner(beaconProtocol, R.array.beacon_protocols, MainActivity.this);
        Utils.fillSpinner(beaconVendor, R.array.vendors, MainActivity.this);
        Utils.fillSpinner(beaconFloorDropdown, R.array.floors, MainActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();
        setupEvents();

        if (googleMap == null) {
            mapFragment = (CustomMapFragment) getFragmentManager()
                    .findFragmentById(R.id.googleMap);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
                mapFragment.setMapTouchEventListener(this);
            }
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        presenter.getBeacons(new ResponseCallback() {
            @Override
            public void onSuccess(Object data) {
                beaconMarkers = new ArrayList<>();
                List<Beacon> beacons = (List<Beacon>) data;
                loadBeacon(beacons);
            }

            @Override
            public void onFailure(String message, String title) {
                UIUtils.showAlertMessage(MainActivity.this, message, title);
            }
        });

        presenter.getZones(new ResponseCallback() {
            @Override
            public void onSuccess(Object data) {
                List<Zone> zones = (List<Zone>) data;
                beaconZoneDropdown.setAdapter(new ZoneArrayAdapter(MainActivity.this,
                        R.layout.zone_spinner_item, zones));
            }

            @Override
            public void onFailure(String message, String title) {
                UIUtils.showAlertMessage(MainActivity.this, message, title);
            }
        });

        UIUtils.setUUIDMask(beaconUUID);
    }

    private void setupEvents() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBeaconItem.setVisible(false);
                addBeaconItem.setVisible(false);
                toggleBeaconDetails(View.GONE, View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapNewBeaconPin.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                beaconNameContainer.setVisibility(View.GONE);
                actionButton.setVisibility(View.GONE);
            }
        });

        notificationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.startSubActivityForResult(MainActivity.this, NotificationActivity.class,
                        NOTIFICATION_LOCATION);
            }
        });

        zoneListContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.startSubActivity(MainActivity.this, ZoneListActivity.class);
            }
        });

        beaconZoneDataContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent(MainActivity.this, ZoneAndFloorActivity.class);
                data.putExtra("floor", beaconFloorNumber.getText().toString());
                data.putExtra("zone_name", beaconZoneName.getText().toString());

                UIUtils.startSubActivityForResult(MainActivity.this, data, ZONE_AND_FLOOR_LOCATION);
            }
        });

        beaconFloorFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MapHelper.filterMarkers(beaconMarkers, MarkerFilterEnum.FLOOR,
                        beaconFloorFilter.getSelectedItem().toString());

                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveBeaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Beacon beacon = new Beacon();
                if (previousSelectedMarker != null) {
                    beacon = (Beacon) previousSelectedMarker.getTag();

                    assert beacon != null;
                    beacon.setZone(selectedZone);
                    beacon.setFloor(selectedFloor);
                }else{
                    beacon.setFloor(String.valueOf(beaconFloorDropdown.getSelectedItemPosition()));
                    beacon.setZone((Zone) beaconZoneDropdown.getSelectedItem());
                }

                beacon.setName(beaconName.getText().toString());
                beacon.setLat(Double.parseDouble(beaconLat.getText().toString()));
                beacon.setLng(Double.parseDouble(beaconLng.getText().toString()));
                beacon.setProximityUUID(beaconUUID.getText().toString());
                beacon.setProtocol(beaconProtocol.getSelectedItem().toString());
                beacon.setVendor(beaconVendor.getSelectedItem().toString());

                if (beaconProtocol.getSelectedItemPosition() == 0) {
                    beacon.getBeaconProximityFields()
                            .add(new BeaconProximityField("major",
                                    beaconMajor.getText().toString()));
                    beacon.getBeaconProximityFields()
                            .add(new BeaconProximityField("minor",
                                    beaconMinor.getText().toString()));
                } else {
                    beacon.getBeaconProximityFields()
                            .add(new BeaconProximityField("interval",
                                    beaconSignalInterval.getText().toString()));
                    beacon.getBeaconProximityFields()
                            .add(new BeaconProximityField("namespace",
                                    beaconNamespace.getText().toString()));
                    beacon.getBeaconProximityFields()
                            .add(new BeaconProximityField("instance",
                                    beaconInstanceId.getText().toString()));
                }

                presenter.saveBeacon(beacon, new ResponseCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        addBeaconMarkerToMap((Beacon) data);
                        createZoneSwitches(zonesById);

                        if (previousSelectedMarker == null)
                            beaconName.setText("");
                        else{
                            selectedZoneImage.setImageBitmap(selectedZone.getSelectedIcon());
                            zoneName.setText(selectedZone.getName());
                            floorNumber.setText(selectedFloor);
                        }

                        selectedZone = null;
                        selectedFloor = null;
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(String message, String title) {
                        UIUtils.showAlertMessage(MainActivity.this, message, title);
                    }
                });
            }
        });

        beaconProtocol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    iBeaconProximityFieldsContainer.setVisibility(View.VISIBLE);
                    eddystoneProximityFieldsContainer.setVisibility(View.GONE);
                } else {
                    iBeaconProximityFieldsContainer.setVisibility(View.GONE);
                    eddystoneProximityFieldsContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void toggleBeaconDetails(int actionButtonVisibility, int beaconDetailVisibility) {
        customAttributes = null;
        actionButton.setVisibility(actionButtonVisibility);
        beaconDetailsContainer.setVisibility(beaconDetailVisibility);

        if (actionButtonVisibility == View.VISIBLE) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);

            if (previousSelectedMarker == null) {
                toggleZoneView(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
            }else {
                toggleZoneView(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
            }
        } else {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            zoneDataContainer.setVisibility(actionButtonVisibility);
            cancelButton.setVisibility(View.GONE);
        }

    }

    private void loadBeacon(List<Beacon> beacons) {
        for (Beacon beacon : beacons) {
            addBeaconMarkerToMap(beacon);
        }
        createZoneSwitches(zonesById);
    }

    private void addBeaconMarkerToMap(Beacon beacon) {
        LatLng beaconLocation = new LatLng(beacon.getLat(), beacon.getLng());
        Marker marker = googleMap.addMarker((new MarkerOptions().position(beaconLocation)
                .flat(true)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(beacon.getZone().getUnselectedIcon())))

        );
        marker.setTag(beacon);
        beaconMarkers.add(marker);

        zonesById.put(beacon.getZone().getId(), beacon.getZone());
    }

    private void createZoneSwitches(LongSparseArray<Zone> zones) {
        for (int index = 0; index < zones.size(); index++) {
            ViewGroup zoneSwitch = (ViewGroup) ((ViewGroup) View.inflate(MainActivity.this, R.layout.zone_switch,
                    toggleZoneContainer)).getChildAt(index);
            Zone entry = zones.valueAt(index);

            final SwitchCompat zoneToggle = zoneSwitch.findViewById(R.id.zone_toggle);
            ((CustomTextView) zoneSwitch.findViewById(R.id.zone_name)).setText(entry.getName());
            zoneSwitch.findViewById(R.id.zone_color_view)
                    .setBackgroundColor(Color.parseColor(entry.getColor()));

            zoneToggle.setTag(entry.getId());
            zoneToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    MapHelper.filterMarkers(beaconMarkers, MarkerFilterEnum.ZONE,
                            zoneToggle.getTag(), isChecked);
                }
            });
        }
    }

    private void setupDrawer() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        CustomTextView userEmail = navigationView.findViewById(R.id.user_email);
        CustomTextView userRole = navigationView.findViewById(R.id.user_role);
        beaconFloorFilter = navigationView.findViewById(R.id.beacon_filter_floor);
        toggleZoneContainer = navigationView.findViewById(R.id.toggle_zone_container);
        CustomButton logoutButton = navigationView.findViewById(R.id.logout_button);

        userEmail.setText(UserHelper.getUser().getEmail());
        userRole.setText("Administrator");
        Utils.fillSpinner(beaconFloorFilter, R.array.floors_filter, MainActivity.this);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.startActivity(MainActivity.this, LoginActivity.class);
            }
        });
    }

    private void toggleZoneView(int visibility) {
        beaconNameContainer.setVisibility(View.VISIBLE);
        actionButton.setVisibility(View.VISIBLE);
        zoneFloorContainer.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        beaconZoneDataContainer.setVisibility(visibility);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) actionButton.getLayoutParams();
        if (visibility == View.VISIBLE) {
            actionButton.setImageResource(R.drawable.settings_mapbtn);
            zoneDataContainer.setVisibility(View.VISIBLE);
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.main_floating_button_margin_big);

            mapNewBeaconPin.setVisibility(View.GONE);
            deleteBeaconItem.setVisible(true);
            cancelButton.setVisibility(View.GONE);
        } else {
            zoneDataContainer.setVisibility(View.GONE);
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.main_floating_button_margin);
        }
        actionButton.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapFragment != null) {
            mapFragment.setMapTouchEventListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (beaconDetailsContainer.getVisibility() == View.VISIBLE) {
            toggleBeaconDetails(View.VISIBLE, View.GONE);
            if (previousSelectedMarker != null)
                deleteBeaconItem.setVisible(true);
            addBeaconItem.setVisible(true);
        } else
            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (!Utils.arePermissionsGranted(this, permissionsNeeded)) {
            ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_LOCATION);
        } else {
            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CameraPosition cameraPosition = googleMap.getCameraPosition();
                            if (!MainActivity.this.isDraggingMap) {
                                beaconLat.setText(String.valueOf(cameraPosition.target.latitude));
                                beaconLng.setText(String.valueOf(cameraPosition.target.longitude));
                            }
                        }
                    }, 200);
                }
            });

            googleMap.clear();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(@Nullable Bundle bundle) {
        if (!Utils.arePermissionsGranted(this, permissionsNeeded)) {
            ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_LOCATION);
        } else {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (currentLocation != null) {
                beaconLat.setText(String.valueOf(currentLocation.getLatitude()));
                beaconLng.setText(String.valueOf(currentLocation.getLongitude()));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        .zoom(15)
                        .build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        toggleZoneView(View.VISIBLE);
        Beacon beacon = (Beacon) marker.getTag();

        int protocol = "iBeacon".equalsIgnoreCase(beacon.getProtocol()) ? 0 : 1;

        selectedZone = beacon.getZone();
        selectedFloor = beacon.getFloor();
        selectedZoneImage.setImageBitmap(beacon.getZone().getSelectedIcon());
        beaconZoneImage.setImageBitmap(beacon.getZone().getSelectedIcon());
        zoneName.setText(beacon.getZone().getName());
        beaconZoneName.setText(beacon.getZone().getName());
        floorNumber.setText(beacon.getFloor());
        beaconFloorNumber.setText(beacon.getFloor());
        beaconName.setText(beacon.getName());
        beaconUUID.setText(beacon.getProximityUUID());
        beaconVendor.setSelection(Utils.getIndexOfArray(MainActivity.this, R.array.vendors,
                beacon.getVendor()));

        if (protocol == 0) {
            beaconMinor.setText(beacon.getProximityFieldValueByName("minor"));
            beaconMajor.setText(beacon.getProximityFieldValueByName("major"));
        } else {
            beaconSignalInterval.setText(beacon.getProximityFieldValueByName("interval"));
            beaconNamespace.setText(beacon.getProximityFieldValueByName("namespace"));
            beaconInstanceId.setText(beacon.getProximityFieldValueByName("instance"));
        }

        beaconLat.setText(String.valueOf(beacon.getLat()));
        beaconLng.setText(String.valueOf(beacon.getLng()));
        beaconProtocol.setSelection(protocol);

        marker.setIcon((BitmapDescriptorFactory.fromBitmap(beacon.getZone().getSelectedIcon())));

        if (previousSelectedMarker != null) {
            Beacon oldBeacon = (Beacon) previousSelectedMarker.getTag();
            previousSelectedMarker.setIcon((BitmapDescriptorFactory
                    .fromBitmap(oldBeacon.getZone().getUnselectedIcon())));
        }
        previousSelectedMarker = marker;


        return true;
    }

    @Override
    public void onMapTouchEventActionDown() {
        this.isDraggingMap = true;
    }

    @Override
    public void onMapTouchEventActionUp() {
        this.isDraggingMap = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case NOTIFICATION_LOCATION:
                if (resultCode != Constants.CANCEL_RESULT) {
                    customAttributes = data;
                }
                break;
            case ZONE_AND_FLOOR_LOCATION:
                if(resultCode != Constants.CANCEL_RESULT){
                    String floor = data.getStringExtra("floor");
                    Zone zone = data.getParcelableExtra("zone");
                    zone.setContext(MainActivity.this);

                    selectedZone = zone;
                    selectedFloor = floor;
                    beaconZoneImage.setImageBitmap(zone.getSelectedIcon());
                    beaconZoneName.setText(zone.getName());
                    beaconFloorNumber.setText(floor);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        addBeaconItem = menu.findItem(R.id.add_beacon_action);
        deleteBeaconItem = menu.findItem(R.id.delete_beacon_action);
        deleteBeaconItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.delete_beacon_action:
                UIUtils.showAlertMessage(MainActivity.this,
                        getResources().getString(R.string.delete_beacon_message),
                        getResources().getString(R.string.delete_beacon_title),
                        getResources().getString(R.string.delete_beacon_ok),
                        getResources().getString(R.string.delete_beacon_cancel), 0,
                        new Callback() {
                            @Override
                            public void execute(Object object) {
                                super.execute(object);
                                if (previousSelectedMarker != null) {
                                    Beacon beacon = (Beacon) previousSelectedMarker.getTag();
                                    presenter.deleteBeacon(beacon.getId(), new ResponseCallback() {
                                        @Override
                                        public void onSuccess(Object data) {
                                            beaconMarkers.remove(previousSelectedMarker);
                                            previousSelectedMarker.remove();

                                            previousSelectedMarker = null;
                                            deleteBeaconItem.setVisible(false);
                                            toggleZoneView(View.GONE);
                                            beaconName.setText("");
                                        }

                                        @Override
                                        public void onFailure(String message, String title) {
                                            UIUtils.showAlertMessage(MainActivity.this, message, title);
                                        }
                                    });
                                }
                            }
                        }, null);
                break;
            case R.id.add_beacon_action:
                actionButton.setImageResource(R.drawable.arrow_mapbtn);
                mapNewBeaconPin.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                deleteBeaconItem.setVisible(false);
                toggleZoneView(View.GONE);
                beaconName.setText("");

                if (previousSelectedMarker != null) {
                    Beacon oldBeacon = (Beacon) previousSelectedMarker.getTag();
                    previousSelectedMarker.setIcon((BitmapDescriptorFactory
                            .fromBitmap(oldBeacon.getZone().getUnselectedIcon())));
                }
                previousSelectedMarker = null;
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
