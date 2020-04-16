package com.sapphire.HealthAssessmentPDMA.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {
    private final Context context;
    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }


    private void init() {

        this.setTextIsSelectable(false);
        this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
        this.setLongClickable(false);
        this.cancelLongPress();

    }

    @Override
    public int getSelectionStart() {

        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println("=====element.getMethodName()::"+element.getMethodName());
            if (element.getMethodName().equals("canPaste")) {
                return -1;
            }
        }
        return super.getSelectionStart();
    }

    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    private class ActionModeCallbackInterceptor implements ActionMode.Callback, android.view.ActionMode.Callback {
        private final String TAG = CustomEditText.class.getSimpleName();

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            menu.clear();
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {

        }
    }
}
