package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sapphire.HealthAssessmentPDMA.AppSignature.AppSignatureHashHelper;
import com.sapphire.HealthAssessmentPDMA.BroadcastReceiver.SMSBroadcastReceiverAPI;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.OTPService;
import com.sapphire.HealthAssessmentPDMA.webService.RegisterUserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class VerifyOTPFragment extends Fragment implements SMSBroadcastReceiverAPI.OTPReceiveListener {

    private EditText edOtpCode;
    private TextView tvOtpError,remainingTimeToResendOTPTV;
    private Button btnVerifyOTP,btnRegenerateOTP;
    private View view;
    private Activity activity;
    private ProgressDialog progressDialog;
    private CommonCode commonCode;
    private String screenName="", userId="", name = "", mobileNo="",cnicString=""
            ,genderString="", ageString="",districtNameString="",
            tehsilNameString, addressString="",latitude="",longitude="",userType="", otpExpireTime="";
    private CountDownTimer countDownTimer = null;
    private Boolean isRequestSent = false,isRegeneratePinClick = false;
    private ScrollView mainScrollView;
    private TextWatcher optCodeTextWatcher;
    private int totalDash = 4;
    SMSBroadcastReceiverAPI smsBroadcastReceiver;
    private String hashKey="", screenNameForOTPRegenerate="";
    private boolean shouldCount = true;
    private long milliseconds = 120000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        activity = (Activity) getContext();
        progressDialog = new ProgressDialog(activity);
        commonCode = new CommonCode(activity);
        init();
        addTextWatcher();
        milliseconds = 120000;

      // Toast.makeText(activity,"hash I l  key "+appSignatureHashHelper.getAppSignatures().get(0),Toast.LENGTH_SHORT).show();
        if (getArguments() != null){
            screenName = getArguments().getString("screen");
            hashKey = getArguments().getString("hashKey");
            if (screenName != null && screenName.length()>0
                    && screenName.equalsIgnoreCase("register")){
                startTimer(milliseconds);
                screenNameForOTPRegenerate = "register";
                name = getArguments().getString("name");
                userId = getArguments().getString("userId");
                mobileNo = getArguments().getString("mobileNo");
                ageString = getArguments().getString("age");
                genderString = getArguments().getString("gender");
                districtNameString = getArguments().getString("districtId");
                tehsilNameString = getArguments().getString("talukaId");
                addressString = getArguments().getString("address");
                cnicString = getArguments().getString("cnic");
                latitude = getArguments().getString("latitude");
                longitude = getArguments().getString("longitude");
                userType = getArguments().getString("user_type");
            }else if (screenName!= null && screenName.equalsIgnoreCase("SignInActivityNotVerified")){
                startTimer(milliseconds);
                screenNameForOTPRegenerate = "register";
                name = getArguments().getString("name");
                userId = getArguments().getString("userId");
                mobileNo = getArguments().getString("mobileNo");
                ageString = getArguments().getString("age");
                genderString = getArguments().getString("gender");
                districtNameString = getArguments().getString("districtId");
                tehsilNameString = getArguments().getString("talukaId");
                addressString = getArguments().getString("address");
                cnicString = getArguments().getString("cnic");
                latitude = getArguments().getString("latitude");
                longitude = getArguments().getString("longitude");
                userType = getArguments().getString("user_type");
                otpExpireTime = getArguments().getString("otpExpireTime");
                commonCode.hideKeyboard(view);
                if (commonCode.isNetworkAvailable()) {
                    //screenName = "register"
                    regenerateOTP(mobileNo, "register",hashKey);
                }else {
                    commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null,false);
                }
            }else {
                commonCode.hideKeyboard(view);
                screenNameForOTPRegenerate = "login";
                mobileNo = getArguments().getString("mobileNo");
                otpExpireTime = getArguments().getString("otpExpireTime");


                if (commonCode.isNetworkAvailable() ) {
                   // if (minutes >=2) {
                    long currentTime = System.currentTimeMillis();
                    long expiryTime = MyDateFormatter.stringToTimeStampWithTime(otpExpireTime).getTime();
                    long minutes2 = TimeUnit.MILLISECONDS.toMinutes(expiryTime - currentTime);
                    long seconds2 = TimeUnit.SECONDS.toSeconds((expiryTime - currentTime)/1000);
                    System.out.println("==================current time " + currentTime+" -- "+expiryTime + " expire " + minutes2 + " --- sec " + seconds2);

                  /*  if (seconds2 <= 0) {*/
                        startTimer(milliseconds);
                        regenerateOTP(mobileNo, "login", hashKey);
                   /* }else {
                        milliseconds = TimeUnit.SECONDS.toMillis(seconds2);
                        startTimer(milliseconds);
                    }*/
                  //  }
                }else {
                    commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null,false);
                }
            }

        }

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateDisplay(new ChangePasswordFragment(),"changePasswordFragmentTax");
                commonCode.hideKeyboard(view);
                if (edOtpCode.getText().toString().length() > 0 && tvOtpError.getVisibility() != View.VISIBLE) {
                    if (commonCode.isNetworkAvailable()) {
                        if (!isRequestSent) {
                            isRequestSent = true;
                            verifyOTPRegister(mobileNo, edOtpCode.getText().toString());
                        }
                    }else {
                        commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null,false);
                    }

                }else if (edOtpCode.getText().toString().length() == 0){
                    tvOtpError.setText("OTP Code is required");
                    tvOtpError.setVisibility(View.VISIBLE);
                }else if (edOtpCode.getText().toString().length() < 4){
                    tvOtpError.setText("Invalid OTP code");
                    tvOtpError.setVisibility(View.VISIBLE);
                }
            }
        });



        btnRegenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCode.hideKeyboard(view);
                if (commonCode.isNetworkAvailable()) {
                    isRegeneratePinClick = true;
                    regenerateOTP(mobileNo, screenNameForOTPRegenerate,hashKey);
                }else {
                    commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null,false);
                }
            }
        });


        mainScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                commonCode.hideKeyboard(view);
                if (edOtpCode.hasFocus() ){
                    edOtpCode.clearFocus();
                }
                //scrollView.performClick();
                return false;
            }
        });

        return view;
    }



    private void startTimer(long milliseconds) {
        disableRegenerateButton();
        enableVerifyButton();
        // disable for two minutes
        countDownTimer = null;
        if(isRegeneratePinClick){
            milliseconds = 120000;
            isRegeneratePinClick = false;
        }
        countDownTimer = new CountDownTimer(milliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(shouldCount){
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;

                    remainingTimeToResendOTPTV.setVisibility(View.VISIBLE);
                    if (seconds < 10) {
                        remainingTimeToResendOTPTV.setText("You can Re-send OTP after " + minutes + ":0" + seconds);
                    } else {
                        remainingTimeToResendOTPTV.setText("You can Re-send OTP after " + minutes + ":" + seconds);
                    }
                }
                //System.out.println("=====tick::"+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {

                if(shouldCount){
                    remainingTimeToResendOTPTV.setText("You can Re-send OTP after 0:00");
                    remainingTimeToResendOTPTV.setVisibility(View.GONE);
                    disableVerifyButton();
                    enableRegenerateButton();
                    if(countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                    countDownTimer = null;
                }

            }
        }.start();

    }



    private void init(){
        btnVerifyOTP = view.findViewById(R.id.btnVerifyOTP);
        edOtpCode = view.findViewById(R.id.edOTPVerifyOTPFrag);
        tvOtpError = view.findViewById(R.id.tvOTPErrorVerifyOTPFrag);
        btnRegenerateOTP = view.findViewById(R.id.btnReGeneratePinOTPFrag);
        mainScrollView = view.findViewById(R.id.scrollviewVerifyOTPFrag);
        remainingTimeToResendOTPTV = view.findViewById(R.id.tv_remaining_time_regenerate_otp_verify_otp_frag);
    }


    private void verifyOTPRegister(final String mobileNo, String token){
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new RegisterUserService(activity).verifyOTP(mobileNo, token, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){

                        try {
                            String statusDescription = response.getString("statusDescription");
                            if(statusDescription.equalsIgnoreCase("Success")){


                                    if (screenName.equalsIgnoreCase("register")) {
                                        Toast.makeText(activity, "Welcome user "+name,Toast.LENGTH_SHORT).show();
                                        new UserSession(activity).createUserSession(userId, name, mobileNo,ageString,genderString,districtNameString,
                                                                                        tehsilNameString,addressString,cnicString,latitude,longitude,userType);
                                        if(countDownTimer!=null){
                                            countDownTimer.cancel();
                                        }
                                        // Remove current Fragment
                                        Fragment userFrag = getActivity().getSupportFragmentManager().findFragmentByTag("UserInformationFragment");

                                        if (userFrag != null && userFrag.isAdded()) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(userFrag).commit();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }

                                            CommonCode.updateDisplay(new DashboardFragment(), getActivity().getSupportFragmentManager());
                                    }else if (screenName.length()>0 && screenName.equalsIgnoreCase("VerifiedLogin")){
                                        if(countDownTimer!=null){
                                            countDownTimer.cancel();
                                            countDownTimer.onFinish();
                                        }
                                        // Remove current Fragment
                                        Fragment userFrag = getActivity().getSupportFragmentManager().findFragmentByTag("VerifyOTPFragment");

                                        if (userFrag != null && userFrag.isAdded()) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(userFrag).commit();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }

                                        CommonCode.updateDisplay(new DashboardFragment(), getActivity().getSupportFragmentManager());

                                    }
                                    else {
                                        new UserSession(activity).createUserSession(userId, name, mobileNo,ageString,genderString,districtNameString,
                                                tehsilNameString,addressString,cnicString,latitude,longitude,userType);
                                        if(countDownTimer!=null){
                                            countDownTimer.cancel();
                                            countDownTimer.onFinish();
                                        }
                                        // Remove current Fragment
                                        Fragment userFrag = getActivity().getSupportFragmentManager().findFragmentByTag("VerifyOTPFragment");

                                        if (userFrag != null && userFrag.isAdded()) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(userFrag).commit();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }

                                        CommonCode.updateDisplay(new DashboardFragment(), getActivity().getSupportFragmentManager());

                                    }


                            }else if(statusDescription.contains("OTP does not match")){
                                commonCode.showErrorORSuccessAlert(activity,"error","OTP code does not match",getActivity().getSupportFragmentManager(),null,false);
                            }else if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("OTP has been Expired")){
                                commonCode.showErrorORSuccessAlert(activity,"error","OTP code has been expired",getActivity().getSupportFragmentManager(),null,false);
                            }
                            else {
                                commonCode.showErrorORSuccessAlert(activity,"error","OTP code already used",getActivity().getSupportFragmentManager(),null,false);
                            }

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            isRequestSent = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            isRequestSent = false;
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                        }
                }else {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    isRequestSent = false;
                    if (userId.length()==0){
                        commonCode.showErrorORSuccessAlert(activity,"error","User not found",getActivity().getSupportFragmentManager(),null,false);
                    }else {
                        commonCode.showErrorORSuccessAlert(activity,"error", "OTP code does not match",getActivity().getSupportFragmentManager(),null,false);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                isRequestSent = false;
                /*if(error.getMessage()!=null && error.getMessage().contains("org.json.JSONException")){
                    commonCode.showAlert(R.drawable.ic_warning_black_24dp,"OTP code does not match");
                }
                else{*/

                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null,false);
//            }
            }
        });
    }
    private void enableRegenerateButton() {
        btnRegenerateOTP.setEnabled(true);
        btnRegenerateOTP.setClickable(true);
        btnRegenerateOTP.setBackgroundResource(R.drawable.btn_black_bg);

    }

    private void disableRegenerateButton() {
        btnRegenerateOTP.setEnabled(false);
        btnRegenerateOTP.setClickable(false);
        btnRegenerateOTP.setBackgroundResource(R.drawable.btn_gray_bg);

    }

    private void enableVerifyButton() {
        btnVerifyOTP.setEnabled(true);
        btnVerifyOTP.setClickable(true);
        btnVerifyOTP.setBackgroundResource(R.drawable.btn_sign_in);

    }

    private void disableVerifyButton() {
        btnVerifyOTP.setEnabled(false);
        btnVerifyOTP.setClickable(false);
        btnVerifyOTP.setBackgroundResource(R.drawable.btn_gray_bg);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("==============on destroy");
        /*if(countDownTimer!=null){
            System.out.println("==============on destroy if");
            //countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        countDownTimer = null;*/
        shouldCount = false;
        System.out.println("==============on destroy if "+countDownTimer);
        if (getActivity() != null) {
            ((LockDrawer) getActivity()).unLockDrawer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (activity != null){
            if(smsBroadcastReceiver!=null){
                activity.unregisterReceiver(smsBroadcastReceiver);
                smsBroadcastReceiver = null;
            }

        }
    }

    private void regenerateOTP(String mobileNo, String screenName, String hashKey){
        new OTPService(activity).resendOTP(mobileNo,screenName,hashKey, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        if (statusDescription != null && statusDescription.length()>0
                                && statusDescription.equalsIgnoreCase("Success")){
                            if(isRegeneratePinClick){
                                startTimer(milliseconds);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                    }
                }else{
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null,false);
            }
        });
    }

    // Added By Hina Hussain on 23-March-2020
    private void addTextWatcher() {

        optCodeTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()!=null){
                    s = s.toString().replaceAll(" -","");
                    s = s.toString().replaceAll("-","");
                }
                if (s.toString() == null || s.toString().length() == 0){
                    tvOtpError.setText("OTP Code is required");
                    tvOtpError.setVisibility(View.VISIBLE);
                }else if (s.toString().length() < 4){
                    tvOtpError.setText("Invalid OTP code");
                    tvOtpError.setVisibility(View.VISIBLE);
                }else {
                    tvOtpError.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Added BY Hina Hussain on 23-March-2020
                edOtpCode.removeTextChangedListener(optCodeTextWatcher);
                String enteredOptCode = edOtpCode.getText().toString().trim();
                enteredOptCode = enteredOptCode.replaceAll(" -","");
                enteredOptCode = enteredOptCode.replaceAll("-","");
                int reqDash = totalDash-enteredOptCode.length();
                String dashString = "";
                for(int i = 1;i<=reqDash;i++){
                    dashString = dashString+" -";
                }
                if(enteredOptCode.length()==4){
                    setEditTextMaxLength(edOtpCode,4);
                }
                else{
                    setEditTextMaxLength(edOtpCode,11);
                }
                edOtpCode.setText(enteredOptCode+dashString);
                edOtpCode.setSelection(enteredOptCode.length());

                if(enteredOptCode.length()==0){
                    edOtpCode.setText("");
                    edOtpCode.setHint("- - - -");
                }

                edOtpCode.addTextChangedListener(optCodeTextWatcher);
            }
        };

    }

    @Override
    public void onPause() {
        super.onPause();
        ((LockDrawer)getActivity()).unLockDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startSMSListener();
        NavigationDrawerActivity.tvHeading.setText("VERIFICATION");
        edOtpCode.addTextChangedListener(optCodeTextWatcher);
        ((LockDrawer)getActivity()).lockDrawer();

    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }


    @Override
    public void onOTPReceived(String message) {
       // Toast.makeText(activity,"Started succesfully"+message,Toast.LENGTH_SHORT).show();
        assert message != null;
        String[] smsBodyArr = message.split(":");
        String smsCode=null;
        if (smsBodyArr!=null && smsBodyArr.length >1){
            smsCode = smsBodyArr[1].trim();

            //if (isAuthenticationLogin){
            //System.out.println("------------sms code "+smsCode.split("\n")[0]);
                edOtpCode.setText(""+smsCode.split("\n")[0]);
            edOtpCode.setSelection(edOtpCode.getText().length());
                //isAuthenticationLogin = false;
                btnVerifyOTP.performClick();
                if(smsBroadcastReceiver!=null){
                    activity.unregisterReceiver(smsBroadcastReceiver);
                    smsBroadcastReceiver = null;
                }

          //  }
        }
    }
    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }

    private void startSMSListener() {
        try {
            smsBroadcastReceiver = new SMSBroadcastReceiverAPI();
            smsBroadcastReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            activity.registerReceiver(smsBroadcastReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(activity);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                  //  Toast.makeText(activity,"Started succesfully",Toast.LENGTH_SHORT).show();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
