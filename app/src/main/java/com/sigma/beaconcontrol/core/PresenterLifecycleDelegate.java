package com.sigma.beaconcontrol.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Wilson on 7/13/17.
 */

public class PresenterLifecycleDelegate<P extends Presenter> {

    private static final String PRESENTER_KEY = "presenter";
    private static final String PRESENTER_ID_KEY = "presenter_id";

    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public void onRestoreInstanceState(Bundle presenterState) {
        if (presenter == null)
            throw new IllegalArgumentException("Presenter cannot be null!!!!!!");
        this.bundle = presenterState;
        presenter.create(presenterState);
    }

    public void onCreate() {
        presenter.create(null);
    }

    public void onResume(Activity activity) {
        if (presenter != null) {
            presenter.addActivity(activity);
            if (presenter.isRecreateOnResume()) {
                presenter.create(null);
            }
        }
    }

    public void onDropActivity() {
        if (presenter != null) {
            presenter.dropActivity();
        }
    }

    public void onDestroy(boolean isFinal) {
        if (presenter != null && isFinal) {
            presenter.destroy();
            presenter = null;
        }
    }

}
