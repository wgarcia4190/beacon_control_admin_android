package com.sigma.beaconcontrol.helpers.general;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;
import com.sigma.beaconcontrol.views.widgets.CustomTextView;
import com.sigma.beaconcontrol.views.widgets.dialogs.CommonDialogEditTextFragment;
import com.sigma.beaconcontrol.views.widgets.dialogs.CommonDialogFragment;

import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Wilson on 7/13/17.
 */

public class UIUtils {


    private static Dialog mProgressDialog;

    public static void startActivity(Activity firstActivity, Class<?> secondActivity) {
        Intent mainIntent = new Intent(firstActivity, secondActivity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        firstActivity.startActivity(mainIntent);
        firstActivity.finish();
    }

    public static void startActivity(Activity firstActivity, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        firstActivity.startActivity(intent);
        firstActivity.finish();
    }

    public static void startSubActivity(Activity firstActivity, Intent mainIntent) {
        firstActivity.startActivity(mainIntent);
    }

    public static void startSubActivity(Activity firstActivity, Class<?> secondActivity) {
        Intent mainIntent = new Intent(firstActivity, secondActivity);
        firstActivity.startActivity(mainIntent);
    }

    public static void startSubActivityForResult(Activity firstActivity, Class<?> secondActivity, int requestCode) {
        Intent mainIntent = new Intent(firstActivity, secondActivity);
        firstActivity.startActivityForResult(mainIntent, requestCode);

        //firstActivity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public static void startSubActivityForResultNoAnim(Activity firstActivity, Class<?> secondActivity, int requestCode) {
        Intent mainIntent = new Intent(firstActivity, secondActivity);
        firstActivity.startActivityForResult(mainIntent, requestCode);
    }

    public static void startSubActivityForResultNoAnim(Activity firstActivity, Intent intent, int requestCode) {
        firstActivity.startActivityForResult(intent, requestCode);
    }

    public static void startSubActivityForResult(Activity firstActivity, Intent intent, int requestCode) {
        firstActivity.startActivityForResult(intent, requestCode);

        //firstActivity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public static CommonDialogFragment showAlertMessage(Activity context, String message, String title) {
        return showAlertMessage(context, message, title, null);
    }

    public static CommonDialogFragment showAlertMessage(Activity context, String message, String title,
                                                        final Callback callback) {
        return showAlertMessage(context, message, title, "Ok", null, 0, callback, null, null);
    }


    public static CommonDialogFragment showAlertMessage(Activity context, String message, String title, String buttonText, int image,
                                                        final Callback callback, final Callback initCallback) {
        return showAlertMessage(context, message, title, buttonText, null, image, callback, null, initCallback);
    }

    public static CommonDialogFragment showAlertMessage(Activity context, String message, String title,
                                                        String positiveButtonText, String negativeButtonText,
                                                        int image, final Callback callbackPositive,
                                                        final Callback negaviteCallback) {

        return showAlertMessage(context, message, title, positiveButtonText, negativeButtonText,
                image, callbackPositive, negaviteCallback, null);

    }

    public static CommonDialogFragment showAlertMessage(Activity context, String message, String title,
                                                        String positiveButtonText, String negativeButtonText,
                                                        int image, final Callback callbackPositive,
                                                        final Callback negaviteCallback, final Callback initCallback) {
        final CommonDialogFragment.DialogBuilder dialogBuilder =
                new CommonDialogFragment.DialogBuilder();
        dialogBuilder.withTitle(title);
        dialogBuilder.withImage(image);
        dialogBuilder.withMessage(message);
        dialogBuilder.withPositiveListener(positiveButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                if (callbackPositive != null) {
                    callbackPositive.execute(null);
                }
            }
        });
        if (negativeButtonText != null) {
            dialogBuilder.withNegativeListener(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                    if (negaviteCallback != null) {
                        negaviteCallback.execute(null);
                    }
                }
            });
        }
        if (initCallback != null) {
            dialogBuilder.withInitCallback(initCallback);
        }
        dialogBuilder.withCancelable(false);
        CommonDialogFragment dialogFragment = dialogBuilder.build();
        dialogFragment.show(context.getFragmentManager(), context.getLocalClassName());

        return dialogFragment;
    }

    public static CommonDialogEditTextFragment showEditTextAlertMessage(Activity context,
                                                                        String message,
                                                                        String title,
                                                                        String positiveButtonText,
                                                                        String negativeButtonText,
                                                                        final Callback callbackPositive){

        final CommonDialogEditTextFragment.DialogBuilder dialogBuilder =
                new CommonDialogEditTextFragment.DialogBuilder();

        final CommonDialogEditTextFragment dialogFragment = dialogBuilder.build();

        dialogBuilder.withTitle(title);
        dialogBuilder.withImage(0);
        dialogBuilder.withMessage(message);
        dialogBuilder.withPositiveListener(positiveButtonText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                if (callbackPositive != null) {
                    callbackPositive.execute(dialogFragment.getEditText().getText().toString());
                }
            }
        });
        if (negativeButtonText != null) {
            dialogBuilder.withNegativeListener(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
        }

        dialogBuilder.withCancelable(false);
        dialogFragment.show(context.getFragmentManager(), context.getLocalClassName());

        return dialogFragment;

    }

    public static void showNoInternetDialog(final Activity activity) {

        showAlertMessage(activity, "Para utilizar esta aplicacion debe estar conectado al Internet",
                "Error de conexion", new Callback() {
                    @Override
                    public void execute(Object parameters) {
                        activity.finish();
                        System.exit(0);
                    }
                });
    }

    public static void onDismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public static void onShowProgress(final Context context, final String message,
                                      final boolean cancelable) {

        if (mProgressDialog == null || !mProgressDialog.isShowing()) {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = new Dialog(context);
                    mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mProgressDialog.setContentView(R.layout.progress_dialog);

                    final int dividerId = mProgressDialog.getContext().getResources()
                            .getIdentifier("android:id/titleDivider", null, null);
                    View divider = mProgressDialog.findViewById(dividerId);
                    if  (divider != null) {
                        divider.setBackground(null);
                    }

                    CustomTextView messageTextView = mProgressDialog.findViewById(R.id.message);
                    mProgressDialog.setCancelable(cancelable);
                    messageTextView.setText(message);
                    try {
                        mProgressDialog.show();
                    } catch (Exception e) {
                        mProgressDialog = null;
                        onShowProgress(context, message, cancelable);
                    }
                }
            });
        }
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
    }

    public static void loadImage(Context context, String url, int errorImage, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(errorImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
    }


    public static void loadCircleImage(Context context, String url, int errorImage, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(errorImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void loadImageAsBitmap(Context context, String url, final Callback callback) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        callback.execute(resource);
                    }
                });
    }


    public static void showGPSEnabledMessage(final Activity context) {
        UIUtils.showAlertMessage(context, "El GPS no está activado. ¿Quieres ir a la " +
                        "pantalla de configuración?", "Configuración del GPS", "Activar GPS", "Cancelar"
                , 0, new Callback() {
                    @Override
                    public void execute(Object object) {
                        super.execute(object);
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                }, new Callback() {
                    @Override
                    public void execute(Object object) {
                        super.execute(object);

                        UIUtils.showAlertMessage(context,
                                "Para utilizar esta aplicación debe habilitar el servicio de GPS", "Información", new Callback() {
                                    @Override
                                    public void execute(Object object) {
                                        super.execute(object);
                                        context.finishAffinity();
                                    }
                                });
                    }
                });
    }

    public static void showBluetoothEnabledMessage(final Activity context) {
        UIUtils.showAlertMessage(context, "El Bluetooth no está activado. ¿Quieres ir a la " +
                        "pantalla de configuración?", "Configuración del Bluetooth", "Activar Bluetooth", "Cancelar"
                , 0, new Callback() {
                    @Override
                    public void execute(Object object) {
                        super.execute(object);
                        Intent myIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        context.startActivity(myIntent);
                    }
                }, new Callback() {
                    @Override
                    public void execute(Object object) {
                        super.execute(object);

                        UIUtils.showAlertMessage(context,
                                "Para utilizar esta aplicación debe habilitar el servicio de Bluetooth", "Información", new Callback() {
                                    @Override
                                    public void execute(Object object) {
                                        super.execute(object);
                                        context.finishAffinity();
                                    }
                                });
                    }
                });
    }

    public static SpannableStringBuilder makeSectionOfTextBold(String text, String textToBold, ClickableSpan span) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (textToBold.length() > 0 && !textToBold.trim().equals("")) {

            String testText = text.toLowerCase(Locale.US);
            String testTextToBold = textToBold.toLowerCase(Locale.US);
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();

            if (startingIndex < 0 || endingIndex < 0) {
                return builder.append(text);
            } else if (startingIndex >= 0 && endingIndex >= 0) {

                builder.append(text);
                builder.setSpan(span, startingIndex, endingIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            return builder.append(text);
        }

        return builder;
    }

    public static void setUUIDMask(CustomEditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            boolean addSeparator;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if(start == 7 || start == 12 || start == 17 || start == 22){
                    addSeparator = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(addSeparator) {
                    addSeparator = false;
                    editable.append("-");
                }

            }
        });
    }
}
