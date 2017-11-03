package com.sigma.beaconcontrol.views.zone;

import com.sigma.beaconcontrol.core.BeaconCtrlActivity;
import com.sigma.beaconcontrol.model.entities.Zone;
import com.sigma.beaconcontrol.presenters.ZonePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 10/22/17.
 */

public class ZoneActivity extends BeaconCtrlActivity<ZonePresenter> {

    protected static List<Zone> zoneList = new ArrayList<>();
}
