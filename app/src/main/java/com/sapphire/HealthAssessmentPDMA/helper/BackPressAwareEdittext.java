package com.sapphire.HealthAssessmentPDMA.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class BackPressAwareEdittext extends EditText {

    private BackPressedListener listener;

    public BackPressAwareEdittext(Context context) {
        super(context);
    }

    public BackPressAwareEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackPressAwareEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            if (listener != null) listener.onImeBack(this);
        }

        return super.dispatchKeyEvent(event);
    }

    public void setOnBackPressedListener(BackPressedListener listener) {
        this.listener = listener;
    }

    public interface BackPressedListener{
        void onImeBack(BackPressAwareEdittext awareEdittext);
    }
}
