package com.sigma.beaconcontrol.views.widgets.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sigma.beaconcontrol.R;
import com.sigma.beaconcontrol.helpers.general.Callback;
import com.sigma.beaconcontrol.views.widgets.CustomEditText;

/**
 * Created by Wilson on 7/13/17.
 */

public class CommonDialogEditTextFragment extends CommonDialogFragment {
    private View.OnClickListener negativeListener;
    private View.OnClickListener positiveListener;
    private String negativeButtonString;
    private String positiveButtonString;
    private String title;
    private String message;
    private int image;
    private CustomEditText editText;
    private Callback callback;

    private TextView messageTextView;

    public static class DialogBuilder extends BaseDialogFragmentBuilder<DialogBuilder, CommonDialogEditTextFragment> {
        public CommonDialogEditTextFragment createInstance() {
            return new CommonDialogEditTextFragment();
        }
    }

    public CommonDialogEditTextFragment() {

    }

    public void setPositiveListener(View.OnClickListener onClickListener) {
        this.positiveListener = onClickListener;
    }

    public void setNegativeListener(View.OnClickListener onClickListener) {
        this.negativeListener = onClickListener;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, 0);
    }

    @NonNull
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.common_edittext_dialog, viewGroup, false);
        initDialog(inflate);
        return inflate;
    }


    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

    protected void initDialog(View view) {
        setDialogTitleView(view);
        setDialogImage(view);
        setDialogMessageView(view);
        setDialogPositiveButtonView(view);
        setDialogNegativeButtonView(view);
        setDialogEditText(view);

        if (callback != null) {
            callback.execute(null);
        }
    }

    public void setDialogNegativeButtonView(View view) {
        Button button = (Button) view.findViewById(R.id.dialog_negative_button);
        CharSequence negativeButtonText = this.negativeButtonString;
        if (this.negativeListener == null || negativeButtonText == null) {
            button.setVisibility(View.GONE);
            return;
        }
        button.setOnClickListener(this.negativeListener);
        button.setText(negativeButtonText);
        button.setTransformationMethod(null);
    }

    public void setDialogEditText(View view){
        editText = (CustomEditText) view.findViewById(R.id.dialog_edit_text);
    }

    public void setDialogPositiveButtonView(View view) {
        Button button = (Button) view.findViewById(R.id.dialog_positive_button);
        CharSequence positiveButtonText = this.positiveButtonString;
        if (this.positiveListener == null || positiveButtonText == null) {
            button.setVisibility(View.GONE);
            return;
        }
        button.setText(positiveButtonText);
        button.setOnClickListener(this.positiveListener);
        button.setTransformationMethod(null);
    }

    public void setDialogMessageView(View view) {
        messageTextView = (TextView) view.findViewById(R.id.dialog_msg);
        CharSequence message = this.message;
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                messageTextView.setText(Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                messageTextView.setText(Html.fromHtml(message.toString()));
            }
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            return;
        }
        messageTextView.setVisibility(View.GONE);
    }

    public void setDialogImage(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_logo);
        int drawableRes = this.image;
        if (drawableRes > 0) {

            imageView.setImageResource(drawableRes);
            view.findViewById(R.id.dialog_logo_container).setVisibility(View.VISIBLE);
            return;
        }
        view.findViewById(R.id.dialog_logo_container).setVisibility(View.GONE);
    }

    public void setDialogTitleView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.dialog_title);
        CharSequence title = this.title;
        if (title != null) {
            textView.setText(title);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public void setNegativeButtonString(String negativeButtonString) {
        this.negativeButtonString = negativeButtonString;
    }


    public void setPositiveButtonString(String positiveButtonString) {
        this.positiveButtonString = positiveButtonString;
    }

    public String getTitle() {
        return title;
    }

    public CustomEditText getEditText(){
        return editText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void updateMessageTextView(String message, int color) {
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                messageTextView.setText(Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                messageTextView.setText(Html.fromHtml(message.toString()));
            }
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());

            messageTextView.setTextColor(ContextCompat.getColor(getActivity(), color));
            return;
        }
        messageTextView.setVisibility(View.GONE);
    }
}
