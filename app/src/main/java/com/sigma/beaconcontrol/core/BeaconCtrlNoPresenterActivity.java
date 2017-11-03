package com.sigma.beaconcontrol.core;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Wilson on 10/16/17.
 */

public class BeaconCtrlNoPresenterActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
        }
    }


    public void init() {
        ButterKnife.bind(this);
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
