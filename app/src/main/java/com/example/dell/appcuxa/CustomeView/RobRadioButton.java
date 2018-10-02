package com.example.dell.appcuxa.CustomeView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RadioButton;

@SuppressLint("AppCompatCustomView")
public class RobRadioButton extends RadioButton {
    public RobRadioButton(Context context) {
        super(context);
        init();
    }

    public RobRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RobRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(tf);
        }
    }
}
