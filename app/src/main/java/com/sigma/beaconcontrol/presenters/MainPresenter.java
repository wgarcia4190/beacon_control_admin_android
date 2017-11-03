package com.sigma.beaconcontrol.presenters;

import com.sigma.beaconcontrol.core.Presenter;
import com.sigma.beaconcontrol.dao.MainDAO;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.service.HttpStatus;
import com.sigma.beaconcontrol.model.entities.Beacon;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.views.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 10/7/17.
 */

public class MainPresenter extends Presenter<MainActivity, MainDAO> {

    public MainPresenter() {
        super();
        setBeaconCtrlDAO(new MainDAO());
    }

    public void getBeacons(final ResponseCallback responseCallback) {
        getBeaconCtrlDAO().getBeacons(UserHelper.getUser().getId(), new Callback() {
            @Override
            public void execute(Object object) {
                try {
                    super.execute(object);
                    JsonResponseParser parser = (JsonResponseParser) object;

                    if (parser.getStatusCode() == HttpStatus.OK) {
                        List<Beacon> beacons = new ArrayList<>();
                        JSONArray data = new JSONArray(parser.getResponse().getString("data"));

                        for (int index = 0; index < data.length(); index++) {
                            Beacon beacon = new Beacon(getActivity());
                            beacon.fromJSON(data.getJSONObject(index));

                            beacons.add(beacon);
                        }
                        responseCallback.onSuccess(beacons);

                    } else
                        responseCallback.onFailure(parser.getMessage(), "Error getting Beacons");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getZones(final ResponseCallback responseCallback) {
        getBeaconCtrlDAO().getZones(UserHelper.getUser().getId(), new Callback() {
            @Override
            public void execute(Object object) {
                try {
                    super.execute(object);
                    JsonResponseParser parser = (JsonResponseParser) object;

                    if (parser.getStatusCode() == HttpStatus.OK) {
                        List<Zone> zones = new ArrayList<>();
                        zones.add(new Zone(getActivity()));
                        JSONArray data = new JSONArray(parser.getResponse().getString("data"));

                        for (int index = 0; index < data.length(); index++) {
                            Zone zone = new Zone(getActivity());
                            zone.fromJSON(data.getJSONObject(index));

                            zones.add(zone);
                        }
                        responseCallback.onSuccess(zones);
                    } else
                        responseCallback.onFailure(parser.getMessage(), "Error getting Zones");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveBeacon(final Beacon beacon, final ResponseCallback responseCallback) {
        getBeaconCtrlDAO().saveBeacon(beacon, new Callback() {
            @Override
            public void execute(Object object) {
                super.execute(object);
                JsonResponseParser parser = (JsonResponseParser) object;

                if (parser.getStatusCode() == HttpStatus.OK)
                    responseCallback.onSuccess(beacon);
                else
                    responseCallback.onFailure(parser.getMessage(), "Error Saving Zones");
            }
        });
    }

    public void deleteBeacon(long beaconId, final ResponseCallback responseCallback){
        getBeaconCtrlDAO().deleteBeacon(beaconId, new Callback() {
            @Override
            public void execute(Object object) {
                super.execute(object);
                JsonResponseParser parser = (JsonResponseParser) object;

                if (parser.getStatusCode() == HttpStatus.OK)
                    responseCallback.onSuccess(null);
                else
                    responseCallback.onFailure(parser.getMessage(), "Error Deleting Beacon");
            }
        });
    }
}
