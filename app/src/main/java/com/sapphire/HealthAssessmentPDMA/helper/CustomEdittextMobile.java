package com.sapphire.HealthAssessmentPDMA.helper;

import android.content.Context;
import android.os.Handler;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * custom edit text
 */
public class CustomEdittextMobile extends AppCompatEditText {

    private Context context;
    private static final String EDITTEXT_ATTRIBUTE_COPY_AND_PASTE = "isCopyPasteDisabled";
    private static final String PACKAGE_NAME = "http://schemas.android.com/apk/res-auto";
    public CustomEdittextMobile(Context context) {
        super(context);
        this.context  = context;
    }

    public CustomEdittextMobile(Context context, AttributeSet attrs) {
        super(context, attrs);
//        EnableDisableCopyAndPaste(context, attrs);
        this.context  = context;
    }

    /**
     * Enable/Disable Copy and Paste functionality on EditText
     *
     * @param context Context object
     * @param attrs   AttributeSet Object
     */
    private void EnableDisableCopyAndPaste(Context context, AttributeSet attrs) {
        boolean isDisableCopyAndPaste = attrs.getAttributeBooleanValue(PACKAGE_NAME,
                EDITTEXT_ATTRIBUTE_COPY_AND_PASTE, false);
        if (isDisableCopyAndPaste && !isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            this.setLongClickable(false);
            this.setOnTouchListener(new BlockContextMenuTouchListener
                    (inputMethodManager));
        }
    }

    @Override
    public void setSelection(int index) {
        int length = getText().toString().length();
        if(index > 4){
            super.setSelection(index);
        }else if(length >= 4 && index < 5){
            super.setSelection(4);
        }else{
            super.setSelection(length);
        }

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(selStart);
    }


    /**
     * Perform Focus Enabling Task to the widget with the help of handler object
     * with some delay
     */
    private void performHandlerAction(final InputMethodManager inputMethodManager) {
        int postDelayedIntervalTime = 25;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CustomEdittextMobile.this.setSelected(true);
                CustomEdittextMobile.this.requestFocusFromTouch();
                inputMethodManager.showSoftInput(CustomEdittextMobile.this,
                        InputMethodManager.RESULT_SHOWN);
            }
        }, postDelayedIntervalTime);
    }

    /**
     * Class to Block Context Menu on double Tap
     * A custom TouchListener is being implemented which will clear out the focus
     * and gain the focus for the EditText, in few milliseconds so the selection
     * will be cleared and hence the copy paste option wil not pop up.
     * the respective EditText should be set with this listener
     */
    public class BlockContextMenuTouchListener implements View.OnTouchListener {
        private static final int TIME_INTERVAL_BETWEEN_DOUBLE_TAP = 30;
        private InputMethodManager inputMethodManager;
        private long lastTapTime = 0;

        BlockContextMenuTouchListener(InputMethodManager inputMethodManager) {
            this.inputMethodManager = inputMethodManager;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("============there "+event.getAction());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                long currentTapTime = System.currentTimeMillis();
                if (lastTapTime != 0 && (currentTapTime - lastTapTime)
                        < TIME_INTERVAL_BETWEEN_DOUBLE_TAP) {
                    CustomEdittextMobile.this.setSelected(false);
                    performHandlerAction(inputMethodManager);
                    return true;
                } else {
                    if (lastTapTime == 0) {
                        lastTapTime = currentTapTime;
                    } else {
                        lastTapTime = 0;
                    }
                    performHandlerAction(inputMethodManager);
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                CustomEdittextMobile.this.setSelected(false);
                performHandlerAction(inputMethodManager);
                return true;
            }
            return false;
        }
    }
}
