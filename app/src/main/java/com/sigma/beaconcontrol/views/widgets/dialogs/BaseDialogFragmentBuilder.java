package com.sigma.beaconcontrol.views.widgets.dialogs;

import android.support.annotation.DrawableRes;
import android.view.View;

import com.sigma.beaconcontrol.helpers.general.AbstractBuilder;
import com.sigma.beaconcontrol.helpers.general.Callback;

/**
 * Created by Wilson on 7/13/17.
 */

public abstract class BaseDialogFragmentBuilder<S extends BaseDialogFragmentBuilder,
        T extends CommonDialogFragment> extends AbstractBuilder<T> {

    public BaseDialogFragmentBuilder() {
        init();
    }

    public S withPositiveListener(String str, View.OnClickListener onClickListener) {
        this.built.setPositiveButtonString(str);
        this.built.setPositiveListener(onClickListener);
        return (S) this;
    }

    public S withNegativeListener(String str, View.OnClickListener onClickListener) {
        this.built.setNegativeButtonString(str);
        this.built.setNegativeListener(onClickListener);
        return (S) this;
    }

    public S withTitle(String str) {
        this.built.setTitle(str);
        return (S) this;
    }

    public S withMessage(String str) {
        this.built.setMessage(str);
        return (S) this;
    }

    public S withImage(@DrawableRes int i) {
        this.built.setImage(i);
        return (S) this;
    }

    public S withCancelable(boolean z) {
        this.built.setCancelable(z);
        return (S) this;
    }

    public S withInitCallback(Callback z) {
        this.built.setCallback(z);
        return (S) this;
    }

    public void dismiss() {
        this.built.dismiss();
    }
}
