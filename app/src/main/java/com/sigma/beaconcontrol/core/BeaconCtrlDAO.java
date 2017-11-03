package com.sigma.beaconcontrol.core;

import android.content.Context;

import com.sigma.beaconcontrol.exceptions.DaoNotContext;
import com.sigma.beaconcontrol.model.http.services.rest.RestHandler;

/**
 * Created by Wilson on 10/7/17.
 */

public abstract class BeaconCtrlDAO {

    private Context context;

    public RestHandler getHttpClient(){
        return BeaconCtrlApplication.getHttpClient();
    }

    public Context getContext() {
        if(context == null)
            throw new DaoNotContext();
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
