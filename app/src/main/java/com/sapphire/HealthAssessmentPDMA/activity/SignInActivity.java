package com.sapphire.HealthAssessmentPDMA.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.AppSignature.AppSignatureHashHelper;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.CustomTypefaceSpan;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.LocationService;
import com.sapphire.HealthAssessmentPDMA.webService.LoginService;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends AppCompatActivity {

    EditText edMobileNo;
    private Button signInBtn, btnRegister;
    private TextView tvMobileNo,tvMobileNoError, tvHeader;;
    private TextWatcher mblTextWatcher;
    private String mobileNoString="", userIdString="",nameString="",cnicString=""
            ,genderString="", ageString="",districtIdString="",districtNameString,selectedLanguage="",
            tehsilNameString="",tehsilIdString="",latitude="",longitude="",userStatus="",userType="",
            ucIdString="",addressString="",hashKeyString="";
    private Activity activity;
    private ProgressDialog progressDialog;
    private CommonCode commonCode;
    private ScrollView mainScrollView;
    private UserSession session;
    private AppSignatureHashHelper appSignatureHashHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


//        Commiting first Changes on 2 April 2020


        activity = this;
        init();
        progressDialog = new ProgressDialog(activity);
        commonCode = new CommonCode(activity);
        session = new UserSession(activity);
        appSignatureHashHelper = new AppSignatureHashHelper(activity);
        hashKeyString = appSignatureHashHelper.getAppSignatures().get(0);
        scrollViewListener();
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.provincial_management_authority));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkGreenColor)),spannableString.length()-5,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvHeader.setText(spannableString);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNoString = edMobileNo.getText().toString();
                commonCode.hideKeyboard(v);
                if (isDataValid()) {
                    if (commonCode.isNetworkAvailable()){
                        loginUserMethod(mobileNoString);
                    }else {
                        commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getSupportFragmentManager(),null);
                    }
                }
            }
        });

       /* btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,NavigationDrawerActivity.class));

            }
        });*/
        mblTextWatcher = new TextWatcher() {
            private int preLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edMobileNo.removeTextChangedListener(mblTextWatcher);
                String str = edMobileNo.getText().toString();
                int currentLength = edMobileNo.getText().toString().length();
                int cursorPosition = edMobileNo.getSelectionStart();
                if(preLength < currentLength){
                    if(currentLength == 1){
                        if(str.equalsIgnoreCase("3")){
                            str = "+92"+str;
                            cursorPosition = cursorPosition+3;
                            tvMobileNoError.setText("Mobile # is Invalid");
                            tvMobileNoError.setVisibility(View.VISIBLE);
                        }else{
                            str = "";
                            cursorPosition = 0;
                            tvMobileNoError.setText("Mobile # is Required");
                            tvMobileNoError.setVisibility(View.VISIBLE);
                        }
                    }else if (currentLength >= 3 && currentLength <13){
                        tvMobileNoError.setText("Mobile # is Invalid");
                        tvMobileNoError.setVisibility(View.VISIBLE);
                    }else if (currentLength == 0){
                        tvMobileNoError.setText("Mobile # is Required");
                        tvMobileNoError.setVisibility(View.VISIBLE);
                    }else {
                        tvMobileNoError.setVisibility(View.GONE);
                    }
                }else{
                    if(currentLength == 3){
                        str = "";
                        cursorPosition = 0;
                        tvMobileNoError.setText("Mobile # is Required");
                        tvMobileNoError.setVisibility(View.VISIBLE);
                    }else if (currentLength >= 3 && currentLength <13){
                        tvMobileNoError.setText("Mobile # is Invalid");
                        tvMobileNoError.setVisibility(View.VISIBLE);
                    }else if (currentLength == 0){
                        tvMobileNoError.setText("Mobile # is Required");
                        tvMobileNoError.setVisibility(View.VISIBLE);
                    }else {
                        tvMobileNoError.setVisibility(View.GONE);
                    }
                }

                edMobileNo.setText(str);
                edMobileNo.setSelection(cursorPosition);
                preLength = edMobileNo.getText().toString().length();
                edMobileNo.addTextChangedListener(mblTextWatcher);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        setCustomFont();
    }



    private void init() {
        mainScrollView = findViewById(R.id.scrollviewSignIn);
        signInBtn = findViewById(R.id.btnSignIn);
        tvMobileNo = findViewById(R.id.tvMobileNoSignIn);
        edMobileNo = findViewById(R.id.edMobileNumberSignIn);
        /*btnRegister = findViewById(R.id.btnRegisterLogin);*/
        tvMobileNoError = findViewById(R.id.tvMobileNoErrorSignIn);
        tvHeader = findViewById(R.id.tvHeadingSignIn);
    }

    private void setCustomFont() {

        Typeface fontEng = Typeface.createFromAsset(getAssets(),"myriad_pro_regular.ttf");
        Typeface fontUrdu = Typeface.createFromAsset(getAssets(),"notonastaliqurdu_regular.ttf");
        Typeface fontSindhi = Typeface.createFromAsset(getAssets(),"sindhi_fonts.ttf");
        // for Mobile No
        if (session.getSelectedLanguage().equalsIgnoreCase("sindhi")) {
            SpannableString spanStringMob = new SpannableString(activity.getResources().getString(R.string.mobile_no_text_sindhi));
            spanStringMob.setSpan(new CustomTypefaceSpan("",fontEng),0,6,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new CustomTypefaceSpan("", fontSindhi), 8, spanStringMob.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new ForegroundColorSpan(Color.RED), spanStringMob.length() - 1, spanStringMob.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobileNo.setText(spanStringMob);
            tvMobileNo.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringMob = new SpannableString(tvMobileNo.getText().toString());
            spanStringMob.setSpan(new CustomTypefaceSpan("", fontEng), 0, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new CustomTypefaceSpan("", fontUrdu), 8, spanStringMob.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new ForegroundColorSpan(Color.RED), spanStringMob.length() - 1, spanStringMob.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobileNo.setText(spanStringMob);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        edMobileNo.addTextChangedListener(mblTextWatcher);
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        edMobileNo.removeTextChangedListener(mblTextWatcher);
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private Boolean isDataValid(){
        Boolean isMobileNo = true;
        if (mobileNoString.trim().length() == 0){
            tvMobileNoError.setText("Mobile # is Required");
            tvMobileNoError.setVisibility(View.VISIBLE);
            isMobileNo = false;
        }else if (mobileNoString.length()<13){
            tvMobileNoError.setText("Mobile # is Invalid");
            tvMobileNoError.setVisibility(View.VISIBLE);
            isMobileNo = false;
        }

        return isMobileNo;
    }

    private void loginUserMethod(final String mobileNo){

        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new LoginService(this).loginUser(mobileNo, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        if (statusDescription.length()>0 &&
                                statusDescription.equalsIgnoreCase("Success") ||
                                statusDescription.equalsIgnoreCase("User is not active against the provided mobile number")){
                            JSONObject jsonObject = response.getJSONObject("data");
                            if (jsonObject.length()>0){

                                userIdString = jsonObject.getString("user_id");
                                nameString = jsonObject.getString("name");
                                mobileNoString = jsonObject.getString("mobile");
                                ageString = jsonObject.getString("age");
                                genderString = jsonObject.getString("gender");
                                districtIdString = jsonObject.getString("district_id");
                                tehsilIdString = jsonObject.getString("taluka_id");
                                addressString = jsonObject.getString("address");
                                cnicString = jsonObject.getString("cnic");
                                selectedLanguage = jsonObject.getString("selected_language");

                                if (!jsonObject.isNull("latitude")) {
                                    latitude = jsonObject.getString("latitude");
                                }
                                if (!jsonObject.isNull("longitude")) {
                                    longitude = jsonObject.getString("longitude");
                                }
                                userStatus = jsonObject.getString("user_status");
                                userType = jsonObject.getString("user_type");
                                if (userStatus.equalsIgnoreCase("1")) {
                                    session.createSession(selectedLanguage);
                                    session.createUserSession(userIdString, nameString, mobileNoString, ageString, genderString, districtNameString,
                                            tehsilNameString, addressString, cnicString,latitude,longitude,userType);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("mobileNo",mobileNoString);
                                    bundle.putString("screen","VerifiedLogin");
                                    bundle.putString("hashKey",hashKeyString);
                                    Intent intent = new Intent(activity, NavigationDrawerActivity.class);
                                    intent.putExtras(bundle);
                                    activity.startActivity(intent);
                                   // commonCode.showErrorORSuccessAlert(activity, "success", "Login Successfully", getSupportFragmentManager(), null);
                                }else {
                                    //VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
                                    session.createSession(selectedLanguage);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId",userIdString);
                                    bundle.putString("name",nameString);
                                    bundle.putString("cnic",cnicString);
                                    bundle.putString("mobileNo",mobileNoString);
                                    bundle.putString("age",ageString);
                                    bundle.putString("gender", genderString);
                                    bundle.putString("districtId", districtNameString);
                                    bundle.putString("talukaId", tehsilNameString);
                                    bundle.putString("ucId", ucIdString);
                                    bundle.putString("address", addressString);
                                    bundle.putString("latitude", latitude);
                                    bundle.putString("longitude", longitude);
                                    bundle.putString("userType",userType);
                                    bundle.putString("screen","SignInActivityNotVerified");
                                    bundle.putString("hashKey",hashKeyString);

                                    Intent intent = new Intent(activity, NavigationDrawerActivity.class);
                                    intent.putExtras(bundle);
                                    activity.startActivity(intent);

                                    //verifyOTPFragment.setArguments(bundle);
                                    //CommonCode.updateDisplay(verifyOTPFragment,getSupportFragmentManager());
                                }
                                //getDistrictsMethod(districtIdString,tehsilIdString,userStatus);
                            }else {
                                progressDialog.dismiss();
                                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong. Please check your login details.",getSupportFragmentManager(),null);
                            }


                        }else if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("User is not active against the provided mobile number")){
                            progressDialog.dismiss();
                            commonCode.showErrorORSuccessAlert(activity,"error","User is not active against the provided mobile number.",getSupportFragmentManager(),null);
                        }else if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("Mobile number does not exist")){
                            //commonCode.showErrorORSuccessAlert(activity,"error","Mobile number does not exist.",getSupportFragmentManager(),null);
                            /*Intent intent = new Intent(activity,NavigationDrawerActivity.class) ;
                            intent.putExtra("mobileNo",mobileNo);*/
                            session.setMobileNo(mobileNo);
                            startActivity(new Intent(activity,LanguageOptionSelectionActivity.class));
                            //activity.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null);
                    }
                }else {
                    System.out.println("===============no resp");
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getSupportFragmentManager(),null);
            }
        });


    }
    private void getDistrictsMethod(final String distId, final String tehsilId,final String userStatus){
        System.out.println("=============in dist");
        new LocationService(activity).getDistricts(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");

                        if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("Success")){
                            JSONObject jsonObject = response.getJSONObject("data");
                            if (jsonObject.length()>0){
                                JSONObject districtsObj = jsonObject.getJSONObject("districts");
                                for (int i=1; i<= districtsObj.length(); i++){
                                    if (!districtsObj.isNull(""+i)) {
                                        JSONObject innerObj = districtsObj.getJSONObject("" + i);
                                        if (distId.equalsIgnoreCase(innerObj.getString("district_id"))) {
                                            districtNameString = (innerObj.getString("district_name"));
                                            getTehsilsByDistrictMethod(distId,tehsilId,userStatus);
                                        }
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getSupportFragmentManager(),null);
            }
        });
    }

    private void getTehsilsByDistrictMethod(final String districtId, final String tehsilIdString,
                                            final String userStatus){
        System.out.println("=============in tehsil");
        new LocationService(activity).getTehsilsByDistrict(districtId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("Success")){
                            JSONObject jsonObject = response.getJSONObject("data");
                            if (jsonObject.length() > 0){
                                JSONObject tehsilsObject = jsonObject.getJSONObject("district_id~"+districtId);
                                if (tehsilsObject.length()>0){
                                    String i=tehsilIdString;

                                        if (!tehsilsObject.isNull(""+i)){
                                            System.out.println("=============inside if");
                                            JSONObject innerObj = tehsilsObject.getJSONObject(""+i);
                                            if (innerObj.length()>0){
                                                tehsilNameString = (innerObj.getString("taluka_name"));

                                                if (userStatus.equalsIgnoreCase("1")) {
                                                    session.createSession(selectedLanguage);
                                                    session.createUserSession(userIdString, nameString, mobileNoString, ageString, genderString, districtNameString,
                                                            tehsilNameString, addressString, cnicString,latitude,longitude,userType);
                                                    commonCode.showErrorORSuccessAlert(activity, "success", "Login Successfully", getSupportFragmentManager(), null);
                                                }else {
                                                    //VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("userId",userIdString);
                                                    bundle.putString("name",nameString);
                                                    bundle.putString("cnic",cnicString);
                                                    bundle.putString("mobileNo",mobileNoString);
                                                    bundle.putString("age",ageString);
                                                    bundle.putString("gender", genderString);
                                                    bundle.putString("districtId", districtNameString);
                                                    bundle.putString("talukaId", tehsilNameString);
                                                    bundle.putString("ucId", ucIdString);
                                                    bundle.putString("address", addressString);
                                                    bundle.putString("latitude", latitude);
                                                    bundle.putString("longitude", longitude);
                                                    bundle.putString("userType",userType);
                                                    bundle.putString("screen","SignInActivityNotVerified");

                                                    Intent intent = new Intent(activity, NavigationDrawerActivity.class);
                                                    intent.putExtras(bundle);
                                                    activity.startActivity(intent);

                                                    //verifyOTPFragment.setArguments(bundle);
                                                    //CommonCode.updateDisplay(verifyOTPFragment,getSupportFragmentManager());
                                                }

                                            }
                                        }
                                }
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null);
                        progressDialog.dismiss();
                    }

                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getSupportFragmentManager(),null);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void scrollViewListener() {

        mainScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonCode.hideKeyboard(v);

                if (edMobileNo.hasFocus()) {
                    edMobileNo.clearFocus();
                }
                return false;
            }
        });
    }
}
