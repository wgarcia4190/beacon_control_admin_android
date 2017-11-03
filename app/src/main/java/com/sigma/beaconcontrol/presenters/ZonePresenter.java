package com.sigma.beaconcontrol.presenters;

import com.sigma.beaconcontrol.core.Presenter;
import com.sigma.beaconcontrol.dao.ZoneDAO;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.service.HttpStatus;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.views.zone.ZoneActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZonePresenter extends Presenter<ZoneActivity, ZoneDAO> {

    public ZonePresenter() {
        super();
        setBeaconCtrlDAO(new ZoneDAO());
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

    public void addZone(Zone zone, final ResponseCallback responseCallback) {
        getBeaconCtrlDAO().addZone(zone, new Callback() {
            @Override
            public void execute(Object object) {
                super.execute(object);
                try {
                    JsonResponseParser parser = (JsonResponseParser) object;

                    if (parser.getStatusCode() == HttpStatus.OK) {
                        long zoneId = new JSONObject(parser.getResponse().getString("data")).getLong("zone_id");
                        responseCallback.onSuccess(zoneId);
                    } else
                        responseCallback.onFailure(parser.getMessage(), "Error saving Zones");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteZone(long zoneId, final ResponseCallback responseCallback) {
        getBeaconCtrlDAO().deleteZone(zoneId, new Callback() {
            @Override
            public void execute(Object object) {
                super.execute(object);

                JsonResponseParser parser = (JsonResponseParser) object;

                if (parser.getStatusCode() == HttpStatus.OK)
                    responseCallback.onSuccess(null);
                else
                    responseCallback.onFailure(parser.getMessage(), "Error deleting Zone");
            }
        });
    }

}
