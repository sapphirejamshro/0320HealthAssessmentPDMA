package com.sapphire.HealthAssessmentPDMA.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

public class LanguageOptionSelectionActivity extends AppCompatActivity {


    private Context context;
    private ImageView urduBtnImgV, engBtnImgV, sindhiBtnImgV;
    private UserSession userSession;
    private TextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_option_selection);
        this.context = this;
        userSession = new UserSession(context);
        init();
        addListners();

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.provincial_management_authority));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkGreenColor)),spannableString.length()-5,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvHeader.setText(spannableString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appColorWhite));
        }
    }

    /*@Override
    public void onBackPressed() {
        closeApp();
    }*/

    private void addListners(){
        urduBtnImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.createSession("Urdu");
                startActivity(new Intent(context,NavigationDrawerActivity.class));
                finish();
//                CommonCode.updateDisplay(new UserInformationFragment(),getSupportFragmentManager());
            }
        });
        engBtnImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.createSession("en");
                startActivity(new Intent(context,NavigationDrawerActivity.class));
                finish();
                //                CommonCode.updateDisplay(new UserInformationFragment(),getSupportFragmentManager());
            }
        });

        sindhiBtnImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.createSession("Sindhi");
//                CommonCode.updateDisplay(new UserInformationFragment(),getSupportFragmentManager());
                startActivity(new Intent(context,NavigationDrawerActivity.class));
                finish();
            }
        });
    }

    private void init(){
        urduBtnImgV = findViewById(R.id.imgV_urdu_btn_lang_opt_frag);
        engBtnImgV = findViewById(R.id.imgV_eng_btn_lang_opt_frag);
        sindhiBtnImgV  = findViewById(R.id.imgV_sindhi_btn_lang_opt_frag);
        tvHeader = findViewById(R.id.tv_title_lang_opt_sel_frag);
    }

    /*private void closeApp(){

        AlertDialog closeAlert;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LanguageOptionSelectionActivity.this,R.style.AlertDialogTheme);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to exit?")
                .setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // NavigationDrawer.this.finish();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        closeAlert = alertDialog.show();
        closeAlert.setCanceledOnTouchOutside(false);

    }*/
}
