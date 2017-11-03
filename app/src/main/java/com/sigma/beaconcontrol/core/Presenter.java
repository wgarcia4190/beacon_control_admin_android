package com.sigma.beaconcontrol.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wilson on 7/13/17.
 */

public class Presenter<T extends Activity, Z extends BeaconCtrlDAO> {

    private boolean recreateOnResume = false;
    private Z beaconCtrlDAO;

    public Presenter() {

    }

    public Presenter(Z beaconCtrlDAO) {
        this.beaconCtrlDAO = beaconCtrlDAO;
    }

    protected boolean isRecreateOnResume() {
        return recreateOnResume;
    }

    protected void setRecreateOnResume(boolean recreateOnResume) {
        this.recreateOnResume = recreateOnResume;
    }

    protected void onCreate(@Nullable Bundle savedState) {
    }

    @Nullable
    private T activity;
    private CopyOnWriteArrayList<OnDestroyListener> onDestroyListeners = new CopyOnWriteArrayList<>();

    private void onDestroy() {
        this.activity = null;
    }

    private void onAddActivity(@NonNull T view) {
        activity = view;
    }

    protected void onDestroyActivity() {
    }

    private interface OnDestroyListener {
        void onDestroy();
    }

    protected void addOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.add(listener);
    }

    protected void removeOnDestroyListener(OnDestroyListener listener) {
        onDestroyListeners.remove(listener);
    }


    @Nullable
    public T getActivity() {
        return activity;
    }

    void create(Bundle bundle) {
        onCreate(bundle);
    }

    void destroy() {
        for (OnDestroyListener listener : onDestroyListeners)
            listener.onDestroy();
        onDestroy();
    }

    public void addActivity(T view) {
        onAddActivity(view);
    }

    void dropActivity() {
        onDestroy();
    }

    public Z getBeaconCtrlDAO() {
        return beaconCtrlDAO;
    }

    public void setBeaconCtrlDAO(Z beaconCtrlDAO) {
        this.beaconCtrlDAO = beaconCtrlDAO;
    }
}
