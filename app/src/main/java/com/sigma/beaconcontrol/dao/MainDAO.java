package com.sigma.beaconcontrol.dao;

import com.sigma.beaconcontrol.core.BeaconCtrlDAO;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.service.HttpMethod;
import com.sigma.beaconcontrol.helpers.service.MapBuilder;
import com.sigma.beaconcontrol.model.entities.Beacon;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.model.http.parsers.Parser;
import com.sigma.beaconcontrol.model.http.services.rest.Callable;

import org.json.JSONException;

/**
 * Created by Wilson on 10/8/17.
 */

public class MainDAO extends BeaconCtrlDAO {

    private static final String BEACON_PREFIX = "/beacon/secure";
    private static final String ZONE_PREFIX = "/zone/secure";
    private static final String GET_BEACONS_ENDPOINT = BEACON_PREFIX.concat("/getBeacons");
    private static final String GET_ZONES_ENDPOINT = ZONE_PREFIX.concat("/getZones");
    private static final String SAVE_BEACON = BEACON_PREFIX.concat("/save-beacon");
    private static final String getDeleteBeaconUrl(long id) {
        return BEACON_PREFIX.concat("/delete-beacon/").concat(String.valueOf(id));
    };

    public void getBeacons(long userId, final Callback callback) {
        getHttpClient().setEndPoint(GET_BEACONS_ENDPOINT)
                .setHttpMethod(HttpMethod.GET)
                .setHeaders(new MapBuilder().addParam("Authorization",
                        UserHelper.getAuthenticationKey()).build())
                .setParameters(new MapBuilder().addParam("userId", String.valueOf(userId)).build())
                .setCookie(UserHelper.getUser().getCookies())
                .call(new Callable<JsonResponseParser>(JsonResponseParser.class) {
                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                        UIUtils.onShowProgress(getContext(), "Loading...", false);
                    }

                    @Override
                    public void onPostExecute(JsonResponseParser parser) throws JSONException {
                        callback.execute(parser);
                        UIUtils.onDismissProgress();
                    }
                });
    }

    public void getZones(long userId, final Callback callback) {
        getHttpClient().setEndPoint(GET_ZONES_ENDPOINT)
                .setHttpMethod(HttpMethod.GET)
                .setHeaders(new MapBuilder().addParam("Authorization",
                        UserHelper.getAuthenticationKey()).build())
                .setParameters(new MapBuilder().addParam("userId", String.valueOf(userId)).build())
                .setCookie(UserHelper.getUser().getCookies())
                .call(new Callable<JsonResponseParser>(JsonResponseParser.class) {
                    @Override
                    public void onPostExecute(JsonResponseParser parser) throws JSONException {
                        callback.execute(parser);
                    }
                });
    }

    public void saveBeacon(Beacon beacon, final Callback callback){
        getHttpClient().setEndPoint(SAVE_BEACON)
                .setHttpMethod(HttpMethod.PUT)
                .setHeaders(new MapBuilder().addParam("Authorization",
                        UserHelper.getAuthenticationKey()).build())
                .setCookie(UserHelper.getUser().getCookies())
                .setBody(beacon.toJson())
                .call(new Callable<JsonResponseParser>(JsonResponseParser.class) {
                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                        UIUtils.onShowProgress(getContext(), "Loading...", false);
                    }
                    @Override
                    public void onPostExecute(JsonResponseParser parser) throws JSONException {
                        callback.execute(parser);
                        UIUtils.onDismissProgress();
                    }
                });
    }

    public void deleteBeacon(long beaconId, final Callback callback){
        getHttpClient().setEndPoint(getDeleteBeaconUrl(beaconId))
                .setHttpMethod(HttpMethod.DELETE)
                .setHeaders(new MapBuilder().addParam("Authorization",
                        UserHelper.getAuthenticationKey()).build())
                .setCookie(UserHelper.getUser().getCookies())
                .call(new Callable<JsonResponseParser>(JsonResponseParser.class) {
                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                        UIUtils.onShowProgress(getContext(), "Loading...", false);
                    }
                    @Override
                    public void onPostExecute(JsonResponseParser parser) throws JSONException {
                        callback.execute(parser);
                        UIUtils.onDismissProgress();
                    }
                });
    }
}
