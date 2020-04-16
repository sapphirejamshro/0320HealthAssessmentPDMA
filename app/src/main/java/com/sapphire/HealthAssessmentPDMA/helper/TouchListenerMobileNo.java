package com.sapphire.HealthAssessmentPDMA.helper;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TouchListenerMobileNo implements View.OnTouchListener{

    private Context context;
    private CustomEdittextMobile mobileNo;
    private InputMethodManager inputMethodManager;

    private static final int TIME_INTERVAL_BETWEEN_DOUBLE_TAP = 30;
    private long lastTapTime = 0;

    public TouchListenerMobileNo(Context context, CustomEdittextMobile mobileNo, InputMethodManager inputMethodManager){
        this.context = context;
        this.mobileNo = mobileNo;
        this.inputMethodManager = inputMethodManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            long currentTapTime = System.currentTimeMillis();
            if (lastTapTime != 0 && (currentTapTime - lastTapTime)
                    < TIME_INTERVAL_BETWEEN_DOUBLE_TAP) {
                mobileNo.setSelected(false);
                performHandlerAction(inputMethodManager);
                System.out.println("---@@## if lastTapTime "+lastTapTime+", currentTapTime "+currentTapTime);
                return true;
            } else {
                System.out.println("---@@## +lelse  lastTapTime "+lastTapTime+", currentTapTime "+currentTapTime);
                if (lastTapTime == 0) {
                    lastTapTime = currentTapTime;
                } else {
                    lastTapTime = 0;
                }
                performHandlerAction(inputMethodManager);
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mobileNo.setSelected(false);
            performHandlerAction(inputMethodManager);
            return true;
        }
        return false;
    }


    private void performHandlerAction(final InputMethodManager inputMethodManager) {
        int postDelayedIntervalTime = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mobileNo.setSelected(true);
                mobileNo.requestFocusFromTouch();
                inputMethodManager.showSoftInput(mobileNo,
                        InputMethodManager.RESULT_SHOWN);
            }
        }, postDelayedIntervalTime);
    }
}
