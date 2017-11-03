package com.sigma.beaconcontrol.views.login;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.core.BeaconCtrlFullScreenActivity;
import com.sigma.beaconcontrol.helpers.general.ResponseCallback;
import com.sigma.beaconcontrol.helpers.general.TextWatcherWrapper;
import com.sigma.beaconcontrol.helpers.general.UIUtils;
import com.sigma.beaconcontrol.helpers.general.Utils;
import com.sigma.beaconcontrol.presenters.LoginPresenter;
import com.sigma.beaconcontrol.views.main.MainActivity;
import com.sigma.beaconcontrol.views.widgets.CustomButton;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;

import butterknife.BindView;

/**
 * Created by Wilson on 10/7/17.
 */

public class LoginActivity extends BeaconCtrlFullScreenActivity<LoginPresenter> {

    @BindView(R.id.email_edit_text)
    public CustomEditText emailEditText;
    @BindView(R.id.password)
    public CustomEditText passwordEditText;
    @BindView(R.id.toggle_eye)
    public AppCompatImageButton toggleEyeButton;
    @BindView(R.id.login_forgot)
    public CustomTextView loginForgot;
    @BindView(R.id.login_button)
    public CustomButton loginButton;

    private PasswordState passwordState = PasswordState.HIDDEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        presenter = new LoginPresenter();
        setupPresenter();

        init();
    }

    private void init() {
        loginForgot.setMovementMethod(LinkMovementMethod.getInstance());
        loginForgot.setText(UIUtils.makeSectionOfTextBold(getResources()
                .getString(R.string.forget_password), getResources()
                .getString(R.string.forget_password_clickable), new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //new ForgetPasswordDialog(LoginActivity.this).show();
                return;
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.createFromAsset(getAssets(),
                        getString(R.string.fonts_location).concat("Raleway-SemiBold.ttf")));
                ds.setColor(Color.parseColor("#000000"));
            }
        }), TextView.BufferType.SPANNABLE);

        setupEvents();
        emailEditText.setText("malabro37@gmail.com");
        passwordEditText.setText("elmalabarista");
    }

    private void setupEvents() {
        toggleEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordState = passwordState.changeState();
                int inputType = 0;
                int color = 0;

                switch (passwordState) {
                    case VISIBLE:
                        inputType = InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                        color = ContextCompat.getColor(LoginActivity.this, R.color.login_button_color);
                        break;
                    case HIDDEN:
                        inputType = InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD;
                        color = ContextCompat.getColor(LoginActivity.this, R.color.black);
                        break;
                }

                passwordEditText.setInputType(inputType);
                passwordEditText.setSelection(passwordEditText.getText().length());
                passwordEditText.setTypeface(Typeface.createFromAsset(getAssets(),
                        getString(R.string.fonts_location).concat("Raleway-Regular.ttf")));

                toggleEyeButton.setColorFilter(color);
            }
        });

        emailEditText.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordEditText.getText().length() > 0 && Utils.isEmailValid(editable.toString())) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && Utils.isEmailValid(emailEditText.getText().toString())) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.logIn(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(), new ResponseCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                UIUtils.startActivity(LoginActivity.this, MainActivity.class);
                            }

                            @Override
                            public void onFailure(String message, String title) {
                                UIUtils.showAlertMessage(LoginActivity.this, message, title);
                            }
                        });
            }
        });
    }

    private enum PasswordState {
        VISIBLE,
        HIDDEN;

        public PasswordState changeState() {
            if (PasswordState.this == VISIBLE) {
                return HIDDEN;
            }
            return VISIBLE;
        }
    }
}
