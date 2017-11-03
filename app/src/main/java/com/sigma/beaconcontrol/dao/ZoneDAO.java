package com.sigma.beaconcontrol.dao;

import com.sigma.beaconcontrol.core.BeaconCtrlDAO;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.service.HttpMethod;
import com.sigma.beaconcontrol.helpers.service.MapBuilder;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.model.http.parsers.Parser;
import com.sigma.beaconcontrol.model.http.services.rest.Callable;

import org.json.JSONException;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneDAO extends BeaconCtrlDAO {

    private static final String ZONE_PREFIX = "/zone/secure";
    private static final String GET_ZONES_ENDPOINT = ZONE_PREFIX.concat("/getZones");
    private static final String SAVE_ZONE_ENDPOINT = ZONE_PREFIX.concat("/save-zone");
    private static final String getDeleteZoneEndpoint(long zoneId) {
        return ZONE_PREFIX.concat("/delete-zone/").concat(String.valueOf(zoneId));
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

    public void addZone(Zone zone, final Callback callback){
        getHttpClient().setEndPoint(SAVE_ZONE_ENDPOINT)
                .setHttpMethod(HttpMethod.PUT)
                .setHeaders(new MapBuilder().addParam("Authorization",
                        UserHelper.getAuthenticationKey()).build())
                .setCookie(UserHelper.getUser().getCookies())
                .setBody(zone.toJson())
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

    public void deleteZone(long zoneId, final Callback callback){
        getHttpClient().setEndPoint(getDeleteZoneEndpoint(zoneId))
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
