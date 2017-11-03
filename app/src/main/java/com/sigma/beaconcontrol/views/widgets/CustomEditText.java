package com.sigma.beaconcontrol.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.sigma.beaconcontrol.R;

import java.util.Locale;


/**
 * Created by Wilson on 7/13/17.
 */

public class CustomEditText extends AppCompatEditText implements CustomComponent {

    public CustomEditText(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode())
            initializeElement(context, attrs);
    }

    public CustomEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);

        if (!isInEditMode())
            initializeElement(context, attrs);
    }

    @Override
    public void initializeElement(final Context context, final AttributeSet set) {
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.CustomTextView);

        for (int index = 0, total = typedArray.getIndexCount(); index < total; index++) {
            int attrId = typedArray.getIndex(index);

            switch (attrId) {
                case R.styleable.CustomTextView_fontPath:
                    String fontName = context.getString(R.string.fonts_location).concat(typedArray.getString(attrId));
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);

                    this.setTypeface(typeface);
                    break;

                case R.styleable.CustomTextView_toUpperCase:
                    this.setText(this.getText().toString().toUpperCase(Locale.US));
                    break;
                default:
                    break;
            }
        }

        typedArray.recycle();
    }

    @Override
    public boolean onSetAlpha(int alpha) {
        setTextColor(Color.argb(alpha, 255, 255, 255));
        return true;
    }
}
