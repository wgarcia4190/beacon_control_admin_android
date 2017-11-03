package com.sigma.beaconcontrol.presenters;

import com.sigma.beaconcontrol.core.Presenter;
import com.sigma.beaconcontrol.dao.LoginDAO;
import com.sigma.beaconcontrol.helpers.entities.UserHelper;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.service.HttpStatus;
import com.sigma.beaconcontrol.model.entities.User;
import com.sigma.beaconcontrol.model.http.parsers.JsonResponseParser;
import com.sigma.beaconcontrol.views.login.LoginActivity;

/**
 * Created by Wilson on 10/7/17.
 */

public class LoginPresenter extends Presenter<LoginActivity, LoginDAO> {

    private static final String COOKIE_HEADER = "Set-Cookie";
    private static final String TOKEN_HEADER = "X-API-TOKEN";

    public LoginPresenter() {
        super();
        setBeaconCtrlDAO(new LoginDAO());
    }

    public void logIn(String email, String password, final ResponseCallback callback) {
        getBeaconCtrlDAO().logIn(email, password, new Callback() {
            @Override
            public void execute(Object object) {
                super.execute(object);
                JsonResponseParser parser = (JsonResponseParser) object;

                if (parser.getStatusCode() == HttpStatus.OK) {
                    User user = new User();
                    user.setToken(parser.getHeaders().get(TOKEN_HEADER).get(0));
                    if (parser.getHeaders().containsKey(COOKIE_HEADER)) {
                        user.setCookies(parser.getHeaders().get(COOKIE_HEADER).get(0));
                    }
                    user.fromJSON(parser.getResponse());

                    UserHelper.setUser(user);
                    callback.onSuccess(null);
                } else
                    callback.onFailure(parser.getMessage(), "Authentication Error");
            }
        });
    }
}
