package com.sigma.beaconcontrol.core;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sigma.beaconcontrol.exceptions.DaoNotInitializedException;

import butterknife.ButterKnife;

/**
 * Created by Wilson on 7/13/17.
 */

public class BeaconCtrlActivity<P extends Presenter> extends AppCompatActivity {

    protected P presenter;
    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate();
    private FragmentManager fragmentManager;

    public P getPresenter() {
        return presenter;
    }

    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
        }
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

    public void showFragment(BeaconCtrlFragment fragment, int containerId, String tag, boolean addToBackStack) {
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (addToBackStack) {
                transaction.replace(containerId, fragment, tag);
                transaction.addToBackStack(null);
            } else {
                transaction.add(containerId, fragment, tag);
            }
            transaction.commit();
        }
    }


}
