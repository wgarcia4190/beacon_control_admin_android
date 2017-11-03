package com.sigma.beaconcontrol.helpers.general;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Wilson on 7/22/17.
 */

public abstract class TextWatcherWrapper implements TextWatcher {


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public abstract void afterTextChanged(Editable editable);
}
