package com.sigma.beaconcontrol.core;


import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by Wilson on 7/13/17.
 */

public class BeaconCtrlFragment<T extends Presenter> extends Fragment {

    protected T presenter;

    public BeaconCtrlFragment() {
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = (T) presenter;
    }

    public void remove() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();
        getFragmentManager().popBackStack();
    }

    public interface OnRemove {
        public void onRemove();
    }
}
