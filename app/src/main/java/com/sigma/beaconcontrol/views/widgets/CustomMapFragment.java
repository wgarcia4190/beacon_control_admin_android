package com.sigma.beaconcontrol.views.widgets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapFragment;
import com.sigma.beaconcontrol.helpers.map.MapTouchEventListener;

/**
 * Created by Wilson on 10/22/17.
 */

public class CustomMapFragment extends MapFragment {

    private View mOriginalView;
    private TouchableWrapper frameLayout;
    private MapTouchEventListener mMapTouchEventListener;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        mOriginalView = super.onCreateView(layoutInflater, viewGroup, savedInstance);

        frameLayout = new TouchableWrapper(getActivity());
        frameLayout.addView(mOriginalView);

        return frameLayout;
    }

    @Override
    public View getView() {
        return mOriginalView;
    }


    public void setMapTouchEventListener(MapTouchEventListener mapTouchEventListener) {
        mMapTouchEventListener = mapTouchEventListener;
    }

    public interface OnTouchListener {
        public abstract void onTouch();
    }

    public class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMapTouchEventListener.onMapTouchEventActionDown();
                    break;
                case MotionEvent.ACTION_UP:
                    mMapTouchEventListener.onMapTouchEventActionUp();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}
