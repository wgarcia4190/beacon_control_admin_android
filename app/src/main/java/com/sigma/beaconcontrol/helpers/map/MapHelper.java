package com.sigma.beaconcontrol.helpers.map;

import com.google.android.gms.maps.model.Marker;
import com.sigma.beaconcontrol.model.entities.Beacon;

import java.util.List;

/**
 * Created by Wilson on 10/20/17.
 */

public class MapHelper {

    public static void filterMarkers(List<Marker> markers, MarkerFilterEnum filterEnum, Object value) {
        MapHelper.filterMarkers(markers, filterEnum, value, false);
    }

    public static void filterMarkers(List<Marker> markers, MarkerFilterEnum filterEnum, Object value, boolean visibility) {
        if (markers != null)
            for (Marker marker : markers) {
                Beacon beacon = (Beacon) marker.getTag();

                switch (filterEnum) {
                    case FLOOR:
                        if ("All".equals(value))
                            marker.setVisible(true);
                        else if (!value.equals(beacon.getFloor()))
                            marker.setVisible(false);
                        else
                            marker.setVisible(true);
                        break;
                    case ZONE:
                        if(beacon.getZone().getId() == (long)value){
                            marker.setVisible(visibility);
                        }
                        break;
                }
            }
    }
}
