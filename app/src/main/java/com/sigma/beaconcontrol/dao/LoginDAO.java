package com.sigma.beaconcontrol.dao;

import com.sigma.beaconcontrol.BuildConfig;
import com.sigma.beaconcontrol.core.BeaconCtrlDAO;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.service.HttpMethod;
import com.sigma.beaconcontrol.helpers.service.MapBuilder;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.model.http.parsers.Parser;
import com.sigma.beaconcontrol.model.http.services.rest.Callable;

import org.json.JSONException;

/**
 * Created by Wilson on 10/7/17.
 */

public class LoginDAO extends BeaconCtrlDAO {

    private static final String PREFIX = "/user";
    private static final String LOGIN_ENDPOINT = PREFIX.concat("/login");

    public void logIn(String email, String password, final Callback callback) {
        getHttpClient().setEndPoint(LOGIN_ENDPOINT)
                .setHttpMethod(HttpMethod.POST)
                .setParameters(new MapBuilder()
                        .addParam("email", email)
                        .addParam("password", password).build())
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
