package com.sapphire.HealthAssessmentPDMA.splashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.SignInActivity;

public class SplashScreenActivity extends AppCompatActivity {


    private View view;
    private Context context;
    private ConstraintLayout mainContainer;
    private Runnable changeScreenThread;
    private Handler handler;
    private boolean isCurrentScreenFirst = true;
    private static final int DELAY_TIME = 5000;
    private TextView infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );*/
        setContentView(R.layout.activity_splash_screen);

        this.context = this;
        init();
        handler = new Handler();
        changeScreenThread = new Runnable() {
            @Override
            public void run() {
                if(isCurrentScreenFirst){

                    setContentView(R.layout.second_splash_screen);
                    isCurrentScreenFirst = false;
                    handler.postDelayed(changeScreenThread, DELAY_TIME);
                    ConstraintLayout constraintLayout = findViewById(R.id.conslayout_main_container_second_splash_screen);
                    TextView tvHeader = findViewById(R.id.tv_title_sec_splash_frag);
                    SpannableString spannableString = new SpannableString(getResources().getString(R.string.provincial_management_authority));
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkGreenColor)),spannableString.length()-5,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvHeader.setText(spannableString);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.appColorWhite));
                    }

                    if(constraintLayout != null){
                        constraintLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(handler != null && changeScreenThread != null){
                                    handler.removeCallbacks(changeScreenThread);
                                    Intent intent = new Intent(context, SignInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        infoTV = findViewById(R.id.tv_info_second_splash_screen);
                        addSpannableCommas();
                    }
                }else{
                    Intent intent = new Intent(context, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

//        handler.postDelayed(changeScreenThread,DELAY_TIME);
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handler != null && changeScreenThread != null && isCurrentScreenFirst){
                    handler.removeCallbacks(changeScreenThread);
                    isCurrentScreenFirst = false;

                    setContentView(R.layout.second_splash_screen);
                    ConstraintLayout constraintLayout = findViewById(R.id.conslayout_main_container_second_splash_screen);
                    TextView tvHeader = findViewById(R.id.tv_title_sec_splash_frag);
                    SpannableString spannableString = new SpannableString(getResources().getString(R.string.provincial_management_authority));
                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkGreenColor)),spannableString.length()-5,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvHeader.setText(spannableString);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.appColorWhite));
                    }
                    if(constraintLayout != null){
                        constraintLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(handler != null && changeScreenThread != null){
                                    handler.removeCallbacks(changeScreenThread);
                                    Intent intent = new Intent(context, SignInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        infoTV = findViewById(R.id.tv_info_second_splash_screen);
                        addSpannableCommas();
                    }
                    handler.postDelayed(changeScreenThread,DELAY_TIME);
                }else if(handler != null && changeScreenThread != null && !isCurrentScreenFirst){
                    handler.removeCallbacks(changeScreenThread);
                    isCurrentScreenFirst = false;
                    Intent intent = new Intent(context, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void init(){
        mainContainer = findViewById(R.id.conslayout_main_container_splash_activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler != null && changeScreenThread != null){
            handler.removeCallbacks(changeScreenThread);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(handler != null && changeScreenThread != null){
            handler.postDelayed(changeScreenThread,DELAY_TIME);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkGreenColor));
        }
    }

    private void addSpannableCommas(){
        if(infoTV != null){
            String text = infoTV.getText().toString();
            Spannable spannableString  = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.appColorGreen)),0,
                    2 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.appColorGreen)),spannableString.length() - 2,
                    spannableString.length() ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.2f),0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.2f),spannableString.length() - 2,spannableString.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            infoTV.setText(spannableString);
        }
    }
}
