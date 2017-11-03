package com.sigma.beaconcontrol.core;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.sigma.beaconcontrol.exceptions.DaoNotInitializedException;

import butterknife.ButterKnife;

/**
 * Created by Wilson on 7/13/17.
 */

public abstract class BeaconCtrlDialogActivity<P extends Presenter> extends FragmentActivity {

    protected P presenter;
    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate();

    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }


    public void setupPresenter() {
        ButterKnife.bind(this);

        if(presenter.getBeaconCtrlDAO() == null)
            throw new DaoNotInitializedException();

        presenter.addActivity(this);
        presenter.getBeaconCtrlDAO().setContext(this);
        presenterDelegate.setPresenter(presenter);
        if (getIntent().getExtras() != null)
            presenterDelegate.onRestoreInstanceState(getIntent().getExtras());
        else
            presenterDelegate.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onDropActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.onDestroy(!isChangingConfigurations());
    }

    protected abstract void configureDialogUI();
}

