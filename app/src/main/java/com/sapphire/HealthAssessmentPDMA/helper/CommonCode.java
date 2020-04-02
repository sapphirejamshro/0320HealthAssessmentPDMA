package com.sapphire.HealthAssessmentPDMA.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherAssessmentFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.VerifyOTPFragment;

public class CommonCode {

    private Context context;

    public CommonCode(Context context) {
        this.context = context;
    }

    public static void updateDisplay(Fragment fragment, FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentDefault,fragment,fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showErrorORSuccessAlert(final Activity activity, final String type,final String message, final FragmentManager fragmentManager, final  Bundle bundle){

        final Dialog alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final View customAlertDialogView =  activity.getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        final ImageButton close = customAlertDialogView.findViewById(R.id.imgB_close_custome_alert_dialog);
        TextView title = customAlertDialogView.findViewById(R.id.tv_title_custome_alert_dialog);
        TextView description = customAlertDialogView.findViewById(R.id.tv_description_custome_alert_dialog);
        Button done = customAlertDialogView.findViewById(R.id.btn_done_custome_alert_dialog);
        if(type.equalsIgnoreCase("error")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                close.setBackground(activity.getResources().getDrawable(R.drawable.cross_btn_bg_custom_error_dialog));
            }else{
                close.setBackgroundResource(R.drawable.cross_btn_bg_custom_error_dialog);
            }
            close.setImageDrawable(activity.getResources().getDrawable(R.drawable.cross_icon));
            title.setText("Failure");
            done.setBackgroundColor(activity.getResources().getColor(R.color.requireRedColor));
            done.setText("OK");
        }else if(type.equalsIgnoreCase("success")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                close.setBackground(activity.getResources().getDrawable(R.drawable.done_btn_bg_custom_success_dialog));

            }else{
                close.setBackgroundResource(R.drawable.done_btn_bg_custom_success_dialog);

            }
            close.setImageDrawable(activity.getResources().getDrawable(R.drawable.done_icon));
            title.setText("Success");
            done.setBackgroundColor(activity.getResources().getColor(R.color.darkGreenColor));
            done.setText("Done");
        }
        description.setText(message);

        alertDialog.setContentView(customAlertDialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        WindowManager.LayoutParams dialogeLayoutParam = new WindowManager.LayoutParams();
        dialogeLayoutParam.copyFrom(alertDialog.getWindow().getAttributes());
        dialogeLayoutParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(dialogeLayoutParam);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(message.equalsIgnoreCase("Registration Successful! Enter OTP for verification") && type.equalsIgnoreCase("success")){
                    Fragment verifyOTPFragment = new VerifyOTPFragment();

                    verifyOTPFragment.setArguments(bundle);
                    new CommonCode(activity).updateDisplay(verifyOTPFragment,fragmentManager);
                }else if(message.equalsIgnoreCase("Login Successfully") && type.equalsIgnoreCase("success")){

                    Intent intent = new Intent(activity, NavigationDrawerActivity.class);
                    intent.putExtra("screen", "SignInActivity");
                    activity.startActivity(intent);
                    activity.finish();

                }
                // Added By Hina on 26-March-2020
                else if(message.equalsIgnoreCase("User information added Successfully") && type.equalsIgnoreCase("success")){
                    updateDisplay(new OtherAssessmentFragment(),fragmentManager);
                }


            }
        });



        final RelativeLayout relativeLayout = customAlertDialogView.findViewById(R.id.relLay_main_container_custom_alert);
        customAlertDialogView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            private boolean indicator = false;
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                customAlertDialogView.removeOnLayoutChangeListener(this);

                int orientation = activity.getResources().getConfiguration().orientation;
                relativeLayout.requestLayout();
                if(right > oldRight){
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        relativeLayout.setPadding(260,0,260,0);
                        customAlertDialogView.findViewById(R.id.linearlayout_cad).requestLayout();
                    }
                }else if(right < oldRight){
                    if(orientation == Configuration.ORIENTATION_PORTRAIT){
                        relativeLayout.setPadding(60,0,60,0);
                        customAlertDialogView.findViewById(R.id.linearlayout_cad).requestLayout();
                    }
                }

                customAlertDialogView.addOnLayoutChangeListener(this);
            }
        });


    }
    // Added on 03-Jan-2020 By Sadaf Khowaja
    public boolean isNetworkAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else if ( connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            connected = false;
        }
        return connected;
    }

    public void showErrorORSuccessAlertWithBack(final Activity activity, final String type,final String message, final FragmentManager fragmentManager){

        final Dialog alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final View customAlertDialogView =  activity.getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        final ImageButton close = customAlertDialogView.findViewById(R.id.imgB_close_custome_alert_dialog);
        TextView title = customAlertDialogView.findViewById(R.id.tv_title_custome_alert_dialog);
        TextView description = customAlertDialogView.findViewById(R.id.tv_description_custome_alert_dialog);
        Button done = customAlertDialogView.findViewById(R.id.btn_done_custome_alert_dialog);
        done.setLongClickable(false);
        done.setTextIsSelectable(false);
        if(type.equalsIgnoreCase("error")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                close.setBackground(activity.getResources().getDrawable(R.drawable.cross_btn_bg_custom_error_dialog));
            }else{
                close.setBackgroundResource(R.drawable.cross_btn_bg_custom_error_dialog);
            }
            close.setImageDrawable(activity.getResources().getDrawable(R.drawable.cross_icon));
            if (message.equalsIgnoreCase("No Records Found!")){
                title.setText("");
            }else {
                title.setText("Failure");
            }
            done.setBackgroundColor(activity.getResources().getColor(R.color.requireRedColor));
            done.setText("OK");
        }else if(type.equalsIgnoreCase("success")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                close.setBackground(activity.getResources().getDrawable(R.drawable.done_btn_bg_custom_success_dialog));

            }else{
                close.setBackgroundResource(R.drawable.done_btn_bg_custom_success_dialog);

            }
            close.setImageDrawable(activity.getResources().getDrawable(R.drawable.done_icon));
            title.setText("Success");
            done.setBackgroundColor(activity.getResources().getColor(R.color.darkGreenColor));
            done.setText("Done");
        }
        description.setText(message);

        alertDialog.setContentView(customAlertDialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        WindowManager.LayoutParams dialogeLayoutParam = new WindowManager.LayoutParams();
        dialogeLayoutParam.copyFrom(alertDialog.getWindow().getAttributes());
        dialogeLayoutParam.width = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(dialogeLayoutParam);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(type.equalsIgnoreCase("error") &&
                        (message.equalsIgnoreCase("Something went wrong, please check your internet connection.")
                        || message.equalsIgnoreCase("Something went wrong, please try again!")
                        || message.equalsIgnoreCase("No Records Found!"))){
                    activity.onBackPressed();
                }


            }
        });



        final RelativeLayout relativeLayout = customAlertDialogView.findViewById(R.id.relLay_main_container_custom_alert);
        customAlertDialogView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            private boolean indicator = false;
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                customAlertDialogView.removeOnLayoutChangeListener(this);

                int orientation = activity.getResources().getConfiguration().orientation;
                relativeLayout.requestLayout();
                if(right > oldRight){
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        relativeLayout.setPadding(260,0,260,0);
                        customAlertDialogView.findViewById(R.id.linearlayout_cad).requestLayout();
                    }
                }else if(right < oldRight){
                    if(orientation == Configuration.ORIENTATION_PORTRAIT){
                        relativeLayout.setPadding(60,0,60,0);
                        customAlertDialogView.findViewById(R.id.linearlayout_cad).requestLayout();
                    }
                }

                customAlertDialogView.addOnLayoutChangeListener(this);
            }
        });

    }


    //Added by kamran on 1 April 2020
    public static int dpToPixels(Context context,int dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);
        System.out.println("---@@## dpi "+densityDpi+", dp "+dp*(densityDpi/160));
        return dp*(densityDpi/160);
    }
}
