package com.sapphire.HealthAssessmentPDMA.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class BackPressAwareAutoTextview extends AutoCompleteTextView {

    private BackPressedListener listener;

    public BackPressAwareAutoTextview(Context context) {
        super(context);
    }

    public BackPressAwareAutoTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackPressAwareAutoTextview(Context context, AttributeSet attrs, int defStyleAttr) {
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
        void onImeBack(BackPressAwareAutoTextview awareEdittext);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }
}
