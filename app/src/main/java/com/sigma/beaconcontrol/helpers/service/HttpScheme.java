package com.sigma.beaconcontrol.helpers.service;

/**
 * Created by Wilson on 7/13/17.
 */

public enum  HttpScheme {
    HTTP("http://"),
    HTTPS("https://");

    private String scheme;

    private HttpScheme(String scheme){
        this.scheme = scheme;
    }

    public String getScheme(){
        return scheme;
    }
}
