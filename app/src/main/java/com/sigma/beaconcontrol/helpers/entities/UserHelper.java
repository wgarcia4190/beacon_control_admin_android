package com.sigma.beaconcontrol.helpers.entities;

import com.sigma.beaconcontrol.model.entities.User;

/**
 * Created by Wilson on 10/7/17.
 */

public class UserHelper {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserHelper.user = user;
    }

    public static String getAuthenticationKey(){
        return "Bearer ".concat(user.getToken());
    }
}
