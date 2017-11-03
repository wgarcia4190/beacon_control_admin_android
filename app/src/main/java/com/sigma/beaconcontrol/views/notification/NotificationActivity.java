package com.sigma.beaconcontrol.views.notification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.core.BeaconCtrlNoPresenterActivity;
import com.sigma.beaconcontrol.helpers.general.Constants;
import com.sigma.beaconcontrol.helpers.general.Utils;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;

import butterknife.BindView;

/**
 * Created by Wilson on 10/16/17.
 */

public class NotificationActivity extends BeaconCtrlNoPresenterActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.notification_trigger_container)
    public LinearLayout notificationTriggerContainer;
    @BindView(R.id.notification_trigger_dropdown)
    public Spinner notificationTriggerDropdown;
    @BindView(R.id.notification_trigger_text)
    public CustomTextView notificationTriggerText;
    @BindView(R.id.notification_message)
    public CustomEditText notificationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        init();
    }

    @Override
    public void init() {
        super.init();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.notification_activity_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Utils.fillSpinner(notificationTriggerDropdown, R.array.triggers_type, this);
        setupEvents();
    }

    private void setupEvents() {
        notificationTriggerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationTriggerDropdown.performClick();
            }
        });

        notificationTriggerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                notificationTriggerText.setText(notificationTriggerDropdown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = notificationMessage.getText().toString();

                if (message.isEmpty())
                    setResult(Constants.CANCEL_RESULT);
                else {
                    Intent data = new Intent();
                    data.putExtra(Constants.CUSTOM_ATTRIBUTE_TYPE, "text");
                    data.putExtra(Constants.CUSTOM_ATTRIBUTE_VALUE, message);
                    data.putExtra(Constants.TRIGGER_TYPE, notificationTriggerText.getText().toString());

                    setResult(Constants.SUCCESS_RESULT, data);
                }
                finish();
            }
        });
    }
}
