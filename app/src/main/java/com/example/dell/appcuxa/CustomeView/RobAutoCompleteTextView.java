package com.example.dell.appcuxa.CustomeView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;

@SuppressLint("AppCompatCustomView")
public class RobAutoCompleteTextView extends AutoCompleteTextView {
    public RobAutoCompleteTextView(Context context) {
        super(context);
        setupView();
    }

    public RobAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public RobAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RobAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RobAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
        setupView();
    }
    public void setupView(){
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Light.ttf");
        RobAutoCompleteTextView.this.setTypeface(typeface);
        RobAutoCompleteTextView.this.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }
}
