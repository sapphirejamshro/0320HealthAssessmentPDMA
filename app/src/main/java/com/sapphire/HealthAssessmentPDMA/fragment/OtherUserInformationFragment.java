package com.sapphire.HealthAssessmentPDMA.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.helper.BackPressAwareEdittext;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.CustomTypefaceSpan;
import com.sapphire.HealthAssessmentPDMA.helper.RequestCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.models.UserModel;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.LocationService;
import com.sapphire.HealthAssessmentPDMA.webService.RegisterUserService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class OtherUserInformationFragment extends Fragment  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Activity activity;
    private View view;
    private Spinner tehsilSpinner,distSpinner, spGender;
    private ArrayAdapter tehsilSpinAdapter, distSpinAdapter, genderAdapter;
    private List<String> genderList;
    private Button submitBtn;
    private TextView tvMobile,tvName,tvAddress,tvCNIC,tvTehsil,tvAge,tvGender,tvDistrict,
            tvNameError, tvMobileNoError, tvAgeError, tvGenderError,tvAddressError,
            tvDistrictError, tvTehsilError, tvCnicError;
    private UserSession session;
    private String selectedLanguage="";
    private int count=0;
    private ScrollView mainScrollView;
    //For Keyboard
    private KeyboardView keyboardview;
    private Keyboard keyboardQwertyFirst, keyboardQwertySecond, keyboardNumbers,
            keyboardPunctuationsFirst, keyboardPunctuationsSecond;
    private EditText commonEditText, edMobileNumber,edAge;
    private BackPressAwareEdittext edName,edAddress,edCnic;
    private CommonCode commonCode;
    private TextWatcher cnicTextWatcher,mblTextWatcher;
    private String userIdString="",nameString="",cnicString="",mobileNoString=""
            ,genderString="", ageString="",districtIdString="",districtNameString="",tehsilIdString="",
            tehsilNameString, ucIdString="",addressString="",userType="";
    private List<String> districtNameList, districtIdsList, tehsilNameList, tehsilIdsList;
    private ProgressDialog progressDialog;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private final int LOCATION_MODE_HIGH_ACCURACY = 3;
    private Location location;
    private GoogleApiClient googleApiClient;
    private OtherUserInformationFragment currentCtx;
    private String latitude="", longitude="";
    private View viewKeyboard, viewSpacing;

    public OtherUserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_other_user_information, container, false);
        activity = (Activity) getContext();
        currentCtx = this;
        session = new UserSession(activity);
        commonCode = new CommonCode(activity);
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(currentCtx)
                .addOnConnectionFailedListener(currentCtx)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        progressDialog = new ProgressDialog(activity);
        districtNameList = new ArrayList<>();
        districtIdsList = new ArrayList<>();
        tehsilNameList = new ArrayList<>();
        tehsilIdsList = new ArrayList<>();
        genderList = new ArrayList<>();
        NavigationDrawerActivity.tvHeading.setText("INFORMATION");
        init();
        addTouchEventsOnSpinner();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

        if (session != null && session.getSelectedLanguage().length()>0){
            selectedLanguage = session.getSelectedLanguage();
        }

        setTehsilAdapter();
        if (commonCode.isNetworkAvailable()) {
            getDistrictsMethod();
        }else {
            setDistrictSpinnerAdapter();
            commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null);
        }
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        keyboardview = NavigationDrawerActivity.keyboardview;
        addFocusedChangeListener();
        addTextWatcher();
        addSpinnerListener();
        setGenderAdapter();
        addSubmitBtnClick();
        setCustomFont();
        scrollViewListener();
        addBackButtonAware();
        addOnEditorActionListeners();

        return view;
    } // end of OnCreateView




    private void init(){
        tehsilSpinner = view.findViewById(R.id.spin_tehsil_OtherUIFrag);
        distSpinner = view.findViewById(R.id.spin_dist_OtherUIFrag);
        submitBtn = view.findViewById(R.id.submitButton_OtherUIFrag);
        tvMobile = view.findViewById(R.id.tv_mbl_no_OtherUIFrag);
        tvName = view.findViewById(R.id.tv_name_OtherUIFrag);
        tvAddress = view.findViewById(R.id.tv_address_OtherUIFrag);
        tvCNIC = view.findViewById(R.id.tv_cnic_OtherUIFrag);
        tvTehsil = view.findViewById(R.id.tv_tehsil_OtherUIFrag);
        tvDistrict = view.findViewById(R.id.tv_dist_OtherUIFrag);
        edName = view.findViewById(R.id.edNameOtherUIFrag);
        edCnic = view.findViewById(R.id.edCnicOtherUIFrag);
        edAddress = view.findViewById(R.id.edAddressOtherUIFrag);
        edMobileNumber = view.findViewById(R.id.edMobileNumber_OtherUIFrag);
        tvAge = view.findViewById(R.id.tvAgeOtherUIFrag);
        tvGender = view.findViewById(R.id.tvGenderOtherUIFrag);
        spGender = view.findViewById(R.id.spGender);
        mainScrollView = view.findViewById(R.id.scrollView_OtherUIFrag);
        edAge = view.findViewById(R.id.edAgeOtherUIFrag);
        tvNameError = view.findViewById(R.id.tvNameErrorOtherUIFrag);
        tvMobileNoError = view.findViewById(R.id.tvMobileNoErrorOtherUIFrag);
        tvAgeError = view.findViewById(R.id.tvAgeErrorOtherUIFrag);
        tvTehsilError = view.findViewById(R.id.tvTehsilErrorOtherUIFrag);
        tvDistrictError = view.findViewById(R.id.tvDistrictErrorOtherUIFrag);
        tvGenderError = view.findViewById(R.id.tvGenderErrorOtherUIFrag);
        tvAddressError = view.findViewById(R.id.tvAddressErrorOtherUIFrag);
        tvCnicError = view.findViewById(R.id.tvCnicErrorOtherUIFrag);
        viewKeyboard = view.findViewById(R.id.viewKeyboard);
        viewSpacing = view.findViewById(R.id.view_spacing_other_user_info_frag);
    }

    private void addFocusedChangeListener() {
        edMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    edName.setFocusable(true);
                    edName.setFocusableInTouchMode(true);
                    commonCode.hideKeyboard(v);
                }else {
                    hideCustomKeyboard();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(edMobileNumber, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }
        });

        edName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    commonEditText = edName;
                    if (selectedLanguage.equalsIgnoreCase("Sindhi")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_sindhi);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_sindhi_next);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_next);
                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }else  if (selectedLanguage.equalsIgnoreCase("Urdu")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_urdu);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_urdu_next);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers_urdu);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_urdu);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_urdu_next);
                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }else if (selectedLanguage.equalsIgnoreCase("en")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_english);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_english_upper_casse);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers_english);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_english);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_english_next);
                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }
                    EditText edittext = (EditText) v;
                    int inType = edittext.getInputType();       // Backup the input type
                    edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.setFocusableInTouchMode(hasFocus);               // Call native handler
                    edittext.setFocusable(hasFocus);
                    edittext.setInputType(inType);
                    showCustomKeyboard(v);// Restore input type

                }else if (!hasFocus){
                    edMobileNumber.setFocusable(true);
                    edMobileNumber.setFocusableInTouchMode(true);
                    hideCustomKeyboard();
                }
            }
        });

        edAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    commonEditText = edAddress;
                    if (selectedLanguage.equalsIgnoreCase("Sindhi")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_sindhi);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_sindhi_next);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_next);

                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }else if (selectedLanguage.equalsIgnoreCase("Urdu")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_urdu);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_urdu_next);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers_urdu);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_urdu);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_urdu_next);

                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }else if (selectedLanguage.equalsIgnoreCase("en")){

                        keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_english);
                        keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_english_upper_casse);
                        keyboardNumbers = new Keyboard(activity,R.xml.numbers_english);
                        keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_english);
                        keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_english_next);

                        keyboardview.setKeyboard(keyboardQwertyFirst);
                        keyboardview.setPreviewEnabled(false);

                        hideCustomKeyboard();
                        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        commonEditText.setTextIsSelectable(true);
                        keyListeners();
                    }
                    EditText edittext = (EditText) v;
                    int inType = edittext.getInputType();       // Backup the input type
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        edittext.setShowSoftInputOnFocus(false);
                    }
                    edittext.setRawInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.setFocusableInTouchMode(hasFocus);               // Call native handler
                    edittext.setFocusable(hasFocus);
                    edittext.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    showCustomKeyboard(v);// Restore input type

                }
            }
        });

        edCnic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hideCustomKeyboard();
                    showKeyboard(v);
                    /*ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewSpacing.getLayoutParams();
                    layoutParams.height = CommonCode.dpToPixels(activity,120);
                    viewSpacing.setLayoutParams(layoutParams);
                    viewSpacing.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainScrollView.smoothScrollTo(0,viewSpacing.getBottom());
                        }
                    }, 250);*/
                }
                else{
                    commonCode.hideKeyboard(v);
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewSpacing.getLayoutParams();
                    layoutParams.height = CommonCode.dpToPixels(activity,50);
                    viewSpacing.setLayoutParams(layoutParams);
                    NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.VISIBLE);
                }
            }
        });
        // Touch Event
        edName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                commonEditText = edName;
                if (selectedLanguage.equalsIgnoreCase("Sindhi")){

                    keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_sindhi);
                    keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_sindhi_next);
                    keyboardNumbers = new Keyboard(activity,R.xml.numbers);
                    keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations);
                    keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_next);

                    keyboardview.setKeyboard(keyboardQwertyFirst);
                    keyboardview.setPreviewEnabled(false);

                    hideCustomKeyboard();
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    commonEditText.setTextIsSelectable(true);
                    keyListeners();
                }else if (selectedLanguage.equalsIgnoreCase("Urdu")){

                    keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_urdu);
                    keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_urdu_next);
                    keyboardNumbers = new Keyboard(activity,R.xml.numbers_urdu);
                    keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_urdu);
                    keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_urdu_next);


                    keyboardview.setKeyboard(keyboardQwertyFirst);
                    keyboardview.setPreviewEnabled(false);

                    hideCustomKeyboard();
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    commonEditText.setTextIsSelectable(true);
                    keyListeners();
                }else if (selectedLanguage.equalsIgnoreCase("en")){

                    keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_english);
                    keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_english_upper_casse);
                    keyboardNumbers = new Keyboard(activity,R.xml.numbers_english);
                    keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_english);
                    keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_english_next);

                    keyboardview.setKeyboard(keyboardQwertyFirst);
                    keyboardview.setPreviewEnabled(false);

                    hideCustomKeyboard();
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    commonEditText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    commonEditText.setTextIsSelectable(true);
                    keyListeners();
                }
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);
                showCustomKeyboard(v);// Restore input type
                edittext.setCursorVisible(true);

                return true;
            }
        });


        edAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    commonCode.hideKeyboard(v);
                    spGender.requestFocus();
                    spGender.setFocusable(true);
                    spGender.setFocusableInTouchMode(true);
                   /* edName.setFocusable(true);
                    edName.setFocusableInTouchMode(true);*/
                }else {
                    hideCustomKeyboard();
                }
            }
        });

        edAge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard(view);
                    hideCustomKeyboard();
                    textView.clearFocus();
                    spGender.requestFocus();
                    spGender.performClick();
                }
                return true;
            }
        });
    }

    private void addTextWatcher() {
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length()>0){
                    tvNameError.setVisibility(View.GONE);
                }

                //Removed by Kamran on 30-Mar-2020
                /*if (s.toString().trim().length() == 0){
                    tvNameError.setVisibility(View.VISIBLE);
                }else {
                    tvNameError.setVisibility(View.GONE);
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.toString().length() > 0){
                    tvAddressError.setVisibility(View.GONE);
                }
                //Removed by Kamran on 30-Mar-2020
                /*if (s.toString().trim().length() == 0){
                    tvAddressError.setVisibility(View.VISIBLE);
                }else {
                    tvAddressError.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 0){
                    tvAgeError.setVisibility(View.GONE);
                }
                if(s.toString().length()>0 && s.toString().equalsIgnoreCase("0")){
                    tvAgeError.setText("Age can't be 0");
                    tvAgeError.setVisibility(View.VISIBLE);
                }
                else{
                    tvAgeError.setVisibility(View.GONE);
                }
                //Removed by Kamran on 30-Mar-2020
                /*if (s.toString().trim().length() == 0){
                    tvAgeError.setVisibility(View.VISIBLE);
                }else {
                    tvAgeError.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnicTextWatcher  = new TextWatcher() {
            private int previousLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                edCnic.removeTextChangedListener(cnicTextWatcher);
                int cursorPosition = edCnic.getSelectionStart();
                String cnicStr = edCnic.getText().toString().trim();
                String validStr = cnicStr.replaceAll("-", "");
                try {
                    Long.parseLong(validStr);
                    if(validStr.length() >=6 && validStr.length() <13){
                        String startStr = validStr.substring(0,5);
                        String endStr = validStr.substring(5);
                        edCnic.setText(startStr+"-"+endStr);
                        if(cursorPosition == 6 && previousLength < cnicStr.length()){
                            cursorPosition++;
                        }else if(cursorPosition == 14 && previousLength > cnicStr.length()){
                            cursorPosition--;
                        }

                    }else if(validStr.length() ==13){
                        String startStr = validStr.substring(0,5);
                        String secStartStr = validStr.substring(5,12);
                        String endStr = validStr.substring(12);
                        edCnic.setText(startStr+"-"+secStartStr+"-"+endStr);
                        if(cursorPosition == 14 && previousLength < cnicStr.length()){
                            cursorPosition ++;
                        }
                    }else if(cnicStr.contains("-")){
                        edCnic.setText(validStr);
                        if(cursorPosition == 6){
                            cursorPosition--;
                        }
                    }
                } catch (Exception e) {
                    //Toast.makeText(getContext(), "Invalid CNIC number", Toast.LENGTH_SHORT).show();
                }

                edCnic.setSelection(cursorPosition);
                String currentText = edCnic.getText() .toString();
                previousLength = currentText.length();
                edCnic.addTextChangedListener(cnicTextWatcher);

                if (currentText == null || currentText.length() == 0){
                    //tvCnicError.setText("CNIC is Required");
                    tvCnicError.setVisibility(View.GONE);
                }else if (currentText.length()<14){
                    tvCnicError.setVisibility(View.VISIBLE);
                    tvCnicError.setText("CNIC is Invalid");
                }
                else if (tvCnicError.getVisibility() == View.VISIBLE) {
                    tvCnicError.setVisibility(View.GONE);
                }
            }
        };

        mblTextWatcher = new TextWatcher() {
            private int preLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edMobileNumber.removeTextChangedListener(mblTextWatcher);
                String str = edMobileNumber.getText().toString();
                int currentLength = edMobileNumber.getText().toString().length();
                int cursorPosition = edMobileNumber.getSelectionStart();
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

                edMobileNumber.setText(str);
                edMobileNumber.setSelection(cursorPosition);
                preLength = edMobileNumber.getText().toString().length();
                edMobileNumber.addTextChangedListener(mblTextWatcher);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    private void addSpinnerListener() {
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderString = genderAdapter.getItem(position).toString();
                if (position == 1){
                    genderString = "Female";
                }else {
                    genderString = "Male";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        distSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtNameString = String.valueOf(distSpinAdapter.getItem(position));
                districtIdString = districtIdsList.get(position);

                if (districtIdString.equalsIgnoreCase("Select District")){
                    districtIdString = "";
                    districtNameString="";
                    tehsilNameList.clear();
                    tehsilIdsList.clear();
                    setTehsilAdapter();
                }else {
                    if (tvDistrictError.getVisibility() == View.VISIBLE){

                        tvDistrictError.setVisibility(View.GONE);
                    }
                    if (commonCode.isNetworkAvailable()) {
                        getTehsilsByDistrictMethod(districtIdString);
                    }else {
                        setTehsilAdapter();
                        commonCode.showErrorORSuccessAlert(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager(),null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tehsilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tehsilNameString = String.valueOf(tehsilSpinAdapter.getItem(position));
                tehsilIdString = tehsilIdsList.get(position);

                if (tehsilIdString.equalsIgnoreCase("Select Tehsil")){
                    tehsilIdString = "";
                    tehsilNameString="";
                }else {
                    if (tvTehsilError.getVisibility() == View.VISIBLE){

                        tvTehsilError.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setGenderAdapter() {
        if(selectedLanguage.equalsIgnoreCase("en")){
            genderList.add("Male");
            genderList.add("Female");
            genderAdapter = new ArrayAdapter<String>(activity,R.layout.custom_spinner_item,genderList);
        }
        else if(selectedLanguage.equalsIgnoreCase("Urdu")){
            genderList.add("مرد");
            genderList.add("عورت");
            genderAdapter = new ArrayAdapter<String>(activity,R.layout.custom_spinner_item_urdu,genderList);
            spGender.setBackgroundResource(R.drawable.spinner_bg_urdu);
        }
        else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
            genderList.add("مرد");
            genderList.add("عورت");
            genderAdapter = new ArrayAdapter<String>(activity,R.layout.custom_spinner_item_sindhi,genderList);
            spGender.setBackgroundResource(R.drawable.spinner_bg_urdu);

        }
        spGender.setAdapter(genderAdapter);
    }

    private void addSubmitBtnClick() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = edName.getText().toString();
                mobileNoString = edMobileNumber.getText().toString();
                ageString = edAge.getText().toString();
                addressString  =edAddress.getText().toString();
                cnicString = edCnic.getText().toString();
                hideCustomKeyboard();
                commonCode.hideKeyboard(view);
             // CommonCode.updateDisplay(new OtherAssessmentFragment(),getActivity().getSupportFragmentManager());

                // Remove current Fragment
                Fragment userFrag = getActivity().getSupportFragmentManager().findFragmentByTag("UserInformationFragment");
                if (isDataValid()) {

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RequestCode.LOCATION_REQUEST_CODE);


                    }else if (!isLocationEnabled()){
                        showLocationAlert("Enable location","Please enable location to mark your attendance."
                                ,"Ok","Cancel");
                        //Toast.makeText(activity,"Please turn on Location to mark your attendance",Toast.LENGTH_SHORT).show();
                    }else {
                        UserModel userModel = new UserModel();
                        userModel.setName(nameString);
                        userModel.setMobileNumber(mobileNoString);
                        userModel.setAge(ageString);
                        userModel.setGender(genderString);
                        userModel.setDistrict(districtIdString);
                        userModel.setTehsil(tehsilIdString);
                        userModel.setUnionCouncil(ucIdString);
                        userModel.setCnic(cnicString);
                        userModel.setAddress(addressString);
                        userModel.setLatitude(latitude);
                        userModel.setLongitude(longitude);

                        String language = "";
                        if (selectedLanguage.equalsIgnoreCase("en")) {
                            language = "en";
                        } else {
                            language = selectedLanguage;
                        }
                        if (commonCode.isNetworkAvailable()) {
                            registerUser(userModel, language);
                        } else {
                            commonCode.showErrorORSuccessAlert(activity, "error", "Please Connect to Internet!", getActivity().getSupportFragmentManager(), null);
                        }
                    }
                }
            }
        });
    }

    private void addOnEditorActionListeners(){
        edCnic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewSpacing.getLayoutParams();
                layoutParams.height = CommonCode.dpToPixels(activity,50);
                viewSpacing.setLayoutParams(layoutParams);
                edCnic.clearFocus();
                return false;
            }
        });
    }

    private void addBackButtonAware() {

        edName.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edName.clearFocus();
                }

            }
        });
        edAddress.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edAddress.clearFocus();
                }

            }
        });

        edCnic.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewSpacing.getLayoutParams();
                layoutParams.height = CommonCode.dpToPixels(activity,50);
                viewSpacing.setLayoutParams(layoutParams);
                edCnic.clearFocus();
            }
        });

    }



    private void setCustomFont() {
        Typeface fontEng = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_regular.ttf");
        Typeface fontUrdu = Typeface.createFromAsset(getActivity().getAssets(),"notonastaliqurdu_regular.ttf");
        Typeface fontSindhi = Typeface.createFromAsset(getActivity().getAssets(),"sindhi_fonts.ttf");
        // for mobile
        if (selectedLanguage.equalsIgnoreCase("sindhi")) {
            SpannableString spanStringMob = new SpannableString(activity.getResources().getString(R.string.mobile_no_text_sindhi));
            spanStringMob.setSpan(new CustomTypefaceSpan("",fontEng),0,6,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new CustomTypefaceSpan("", fontSindhi), 8, spanStringMob.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new ForegroundColorSpan(Color.RED), spanStringMob.length() - 1, spanStringMob.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobile.setText(spanStringMob);
            tvMobile.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringMob = new SpannableString(tvMobile.getText().toString());
            spanStringMob.setSpan(new CustomTypefaceSpan("",fontEng),0,6,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new CustomTypefaceSpan("", fontUrdu), 8, spanStringMob.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMob.setSpan(new ForegroundColorSpan(Color.RED), spanStringMob.length() - 1, spanStringMob.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobile.setText(spanStringMob);
        }

        //for Name
        if (selectedLanguage.equalsIgnoreCase("sindhi")){
            SpannableString spanStringName = new SpannableString(activity.getResources().getString(R.string.name_text_sindhi));
            spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("",fontSindhi),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new ForegroundColorSpan(Color.RED),spanStringName.length()-1,spanStringName.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvName.setText(spanStringName);
            tvName.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringName = new SpannableString(tvName.getText().toString());
            spanStringName.setSpan(new CustomTypefaceSpan("", fontEng), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("", fontUrdu), 5, spanStringName.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new ForegroundColorSpan(Color.RED), spanStringName.length() - 1, spanStringName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvName.setText(spanStringName);
        }
        //for Address
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringAddress = new SpannableString(activity.getResources().getString(R.string.address_text_sindhi));
            spanStringAddress.setSpan(new CustomTypefaceSpan("",fontEng),0,7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAddress.setSpan(new CustomTypefaceSpan("",fontSindhi),8,spanStringAddress.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAddress.setSpan(new ForegroundColorSpan(Color.RED),spanStringAddress.length()-1,spanStringAddress.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvAddress.setText(spanStringAddress);
            tvAddress.setPadding(8,20,8,8);

        }else {
            SpannableString spanStringAddress = new SpannableString(tvAddress.getText().toString());
            spanStringAddress.setSpan(new CustomTypefaceSpan("",fontEng),0,7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAddress.setSpan(new CustomTypefaceSpan("",fontUrdu),8,spanStringAddress.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAddress.setSpan(new ForegroundColorSpan(Color.RED),spanStringAddress.length()-1,spanStringAddress.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvAddress.setText(spanStringAddress);

        }
        // for CNIC
        if (selectedLanguage.equalsIgnoreCase("sindhi")){
            SpannableString spanStringCNIC = new SpannableString(activity.getResources().getString(R.string.cnic_text_sindhi));
            spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontSindhi),6,spanStringCNIC.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //spanStringCNIC.setSpan(new ForegroundColorSpan(Color.RED),spanStringCNIC.length()-1,spanStringCNIC.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvCNIC.setText(spanStringCNIC);
            tvCNIC.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringCNIC = new SpannableString(tvCNIC.getText().toString());
            spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontUrdu),6,spanStringCNIC.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            //spanStringCNIC.setSpan(new ForegroundColorSpan(Color.RED),spanStringCNIC.length()-1,spanStringCNIC.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvCNIC.setText(spanStringCNIC);
        }

        // for Tehsil
        if (selectedLanguage.equalsIgnoreCase("sindhi")) {
            SpannableString spanStringTehsil = new SpannableString(activity.getResources().getString(R.string.tehsil_text_sindhi));
            spanStringTehsil.setSpan(new CustomTypefaceSpan("", fontEng), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringTehsil.setSpan(new CustomTypefaceSpan("", fontSindhi), 6, spanStringTehsil.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringTehsil.setSpan(new ForegroundColorSpan(Color.RED), spanStringTehsil.length() - 1, spanStringTehsil.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvTehsil.setText(spanStringTehsil);
            tvTehsil.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringTehsil = new SpannableString(tvTehsil.getText().toString());
            spanStringTehsil.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringTehsil.setSpan(new CustomTypefaceSpan("",fontUrdu),6,spanStringTehsil.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringTehsil.setSpan(new ForegroundColorSpan(Color.RED),spanStringTehsil.length()-1,spanStringTehsil.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvTehsil.setText(spanStringTehsil);
        }

        //for District
        if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
            SpannableString spanStringDistrict = new SpannableString(activity.getResources().getString(R.string.dist_text_sindhi));
            spanStringDistrict.setSpan(new CustomTypefaceSpan("", fontEng), 0, 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringDistrict.setSpan(new CustomTypefaceSpan("", fontSindhi), 10, spanStringDistrict.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringDistrict.setSpan(new ForegroundColorSpan(Color.RED), spanStringDistrict.length() - 1, spanStringDistrict.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvDistrict.setText(spanStringDistrict);
            tvDistrict.setPadding(8,20,8,8);
        }else{
            SpannableString spanStringDistrict = new SpannableString(tvDistrict.getText().toString());
            spanStringDistrict.setSpan(new CustomTypefaceSpan("",fontEng),0,8,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringDistrict.setSpan(new CustomTypefaceSpan("",fontUrdu),10,spanStringDistrict.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringDistrict.setSpan(new ForegroundColorSpan(Color.RED),spanStringDistrict.length()-1,spanStringDistrict.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvDistrict.setText(spanStringDistrict);
        }

        // for Age
        if (selectedLanguage.equalsIgnoreCase("sindhi")) {
            SpannableString spanStringAge = new SpannableString(activity.getResources().getString(R.string.age_sindhi));
            spanStringAge.setSpan(new CustomTypefaceSpan("", fontEng), 0, 3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAge.setSpan(new CustomTypefaceSpan("", fontSindhi), 5, spanStringAge.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAge.setSpan(new ForegroundColorSpan(Color.RED), spanStringAge.length() - 1, spanStringAge.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvAge.setText(spanStringAge);
            tvAge.setPadding(8,20,8,8);
        }else {
            SpannableString spanStringAge = new SpannableString(tvAge.getText().toString());
            spanStringAge.setSpan(new CustomTypefaceSpan("",fontEng),0,3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAge.setSpan(new CustomTypefaceSpan("",fontUrdu),5,spanStringAge.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringAge.setSpan(new ForegroundColorSpan(Color.RED),spanStringAge.length()-1,spanStringAge.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvAge.setText(spanStringAge);
        }

        // for Gender
        if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
            SpannableString spanStringGender = new SpannableString(activity.getResources().getString(R.string.gender_sindhi));
            spanStringGender.setSpan(new CustomTypefaceSpan("", fontEng), 0, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new CustomTypefaceSpan("", fontSindhi), 8, spanStringGender.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new ForegroundColorSpan(Color.RED), spanStringGender.length() - 1, spanStringGender.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvGender.setText(spanStringGender);
            spGender.setPadding(0,13,0,13);
            tvGender.setPadding(8,20,8,8);
        }else if (selectedLanguage.equalsIgnoreCase("urdu")){
            SpannableString spanStringGender = new SpannableString(tvGender.getText().toString());
            spanStringGender.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new CustomTypefaceSpan("",fontUrdu),8,spanStringGender.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new ForegroundColorSpan(Color.RED),spanStringGender.length()-1,spanStringGender.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvGender.setText(spanStringGender);
            spGender.setPadding(0,-8,0,3);
            //tvGender.setPadding(8,20,8,8);

        }else {
            SpannableString spanStringGender = new SpannableString(tvGender.getText().toString());
            spanStringGender.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new CustomTypefaceSpan("",fontUrdu),8,spanStringGender.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringGender.setSpan(new ForegroundColorSpan(Color.RED),spanStringGender.length()-1,spanStringGender.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvGender.setText(spanStringGender);
        }


    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //For Keyboard Added by Sadaf Khowaja
    public void showCustomKeyboard( View v ) {
        keyboardview.setVisibility(View.VISIBLE);
        keyboardview.setEnabled(true);
        if( v!=null ) ((InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        if(NavigationDrawerActivity.isBackPressForKeyboard){
            hideCustomKeyboard();
        }
        int height =0;
        for(Keyboard.Key key : keyboardQwertyFirst.getKeys()) {
            height += key.height;
        }
        if (edAddress.hasFocus()) {
            final int finalHeight = height;
            keyboardview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);
                    mainScrollView.smoothScrollTo(0, mainScrollView.getBottom());
                }
            },250);

        }
    }
    public void hideCustomKeyboard() {
        keyboardview.setVisibility(View.GONE);
        keyboardview.setEnabled(false);
    }
    public void playClick(int keyCode){
        AudioManager am = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    public void keyListeners(){
        keyboardview.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
                if(primaryCode == -101)
                {
                    InputMethodManager imeManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
                    imeManager.showInputMethodPicker();
                }

                if (primaryCode==500 || primaryCode==-101 || primaryCode==32 || primaryCode==-5 || primaryCode==-4 || primaryCode==505 || primaryCode==502){

                } else {
                    keyboardview.setPreviewEnabled(false);
                }
            }

            @Override
            public void onRelease(int primaryCode) {

            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                /*InputMethodService inputMethodService = new InputMethodService();
                InputConnection ic = inputMethodService.getCurrentInputConnection();
*/
                InputConnection ic = getEditText().onCreateInputConnection(new EditorInfo());
                playClick(primaryCode);
                switch (primaryCode) {

                    case Keyboard.KEYCODE_DELETE:
                        ic.deleteSurroundingText(1, 0);
                        break;
                    case Keyboard.KEYCODE_SHIFT:
                        count++;
                        if (count == 1) {
                            keyboardview.setKeyboard(keyboardQwertySecond);
                            keyboardview.setPreviewEnabled(false);
                            keyboardview.setOnKeyboardActionListener(this);
                        }
                        else {
                            count = 0;
                            keyboardview.setKeyboard(keyboardQwertyFirst);
                            keyboardview.setPreviewEnabled(false);
                            keyboardview.setOnKeyboardActionListener(this);
                        }
                        break;
                    case Keyboard.KEYCODE_DONE:

                        String ids[] = commonEditText.getResources().getResourceName(commonEditText.getId()).split("/");
                        String id="";
                        if (ids.length>1){
                            id = ids[1];
                        }
                        if (id.equalsIgnoreCase("edNameOtherUIFrag")){
                            edName.clearFocus();
                            edMobileNumber.requestFocus();
                            edMobileNumber.setFocusable(true);
                            edMobileNumber.setFocusableInTouchMode(true);
                            hideCustomKeyboard();
                        }else {
                            ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        }


                        break;
                    default:
                        char code = (char) primaryCode;
                        /*if (Character.isLetter(code) && caps) {
                            code = Character.toUpperCase(code);
                        }*/
                      /* if (primaryCode == -4){
                           System.out.println("================ed "+commonEditText.getResources().getResourceName(commonEditText.getId()).split("/"));
                       }*/

                        if (primaryCode == 506 || primaryCode == 500 || primaryCode == -101 || primaryCode == 502
                                || primaryCode == 505 || primaryCode == 510 || primaryCode == 511) {
                            if (primaryCode == 502) {
                                keyboardview.setKeyboard(keyboardNumbers);
                                keyboardview.setPreviewEnabled(false);
                                keyboardview.setOnKeyboardActionListener(this);
                            }
                            if (primaryCode == 505) {
                                count=0;
                                keyboardview.setKeyboard(keyboardQwertyFirst);
                                keyboardview.setPreviewEnabled(false);
                                keyboardview.setOnKeyboardActionListener(this);
                            }
                            if (primaryCode == 506) {
                                count=0;

                                keyboardview.setKeyboard(keyboardPunctuationsFirst);
                                keyboardview.setPreviewEnabled(false);
                                keyboardview.setOnKeyboardActionListener(this);

                            }

                            if (primaryCode == 510){

                                keyboardview.setKeyboard(keyboardPunctuationsSecond);
                                keyboardview.setPreviewEnabled(false);
                                keyboardview.setOnKeyboardActionListener(this);
                            }
                            if (primaryCode == 511){

                                keyboardview.setKeyboard(keyboardPunctuationsFirst);
                                keyboardview.setPreviewEnabled(false);
                                keyboardview.setOnKeyboardActionListener(this);
                            }
                        } else {
                            //db.getInstance(getApplicationContext()).insertData_words(String.valueOf(code));
                            ic.commitText(String.valueOf(code), 1);

                        }
                }
            }

            @Override
            public void onText(CharSequence text) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });
    }

    public EditText getEditText(){
        return commonEditText;
    }

    // Added By Hina Hussain on 21-March-2020

    @SuppressLint("ClickableViewAccessibility")
    private void scrollViewListener() {

        mainScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonCode.hideKeyboard(v);
                hideCustomKeyboard();

                if (edMobileNumber.hasFocus() || edAge.hasFocus() || edName.hasFocus()
                        || edAddress.hasFocus() || edCnic.hasFocus() ) {
                    edMobileNumber.clearFocus();
                    edAge.clearFocus();
                    edName.clearFocus();
                    edAddress.clearFocus();
                    edCnic.clearFocus();
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        edCnic.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        edCnic.addTextChangedListener(cnicTextWatcher);
        edMobileNumber.addTextChangedListener(mblTextWatcher);
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        edCnic.removeTextChangedListener(cnicTextWatcher);
        edMobileNumber.removeTextChangedListener(mblTextWatcher);
        if(keyboardview.getVisibility() == View.VISIBLE || viewKeyboard.getVisibility() == View.VISIBLE){
            hideCustomKeyboard();
        }
    }

    private void getDistrictsMethod(){
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        districtIdsList = new ArrayList<>();
        districtNameList = new ArrayList<>();
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
                                districtNameList.add("Select District");
                                districtIdsList.add("Select District");
                                for (int i=1; i<= districtsObj.length(); i++){
                                    if (!districtsObj.isNull(""+i)) {
                                        JSONObject innerObj = districtsObj.getJSONObject("" + i);
                                        districtIdsList.add(innerObj.getString("district_id"));
                                        districtNameList.add(innerObj.getString("district_name"));
                                    }
                                }

                            }
                        }
                        progressDialog.dismiss();
                        setDistrictSpinnerAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
            }
        });
    }

    private void setDistrictSpinnerAdapter(){
        if (districtNameList.size() == 0){
            districtNameList.add("Select District");
        }
        if (districtIdsList.size() == 0){
            districtIdsList.add("Select District");
        }
        distSpinAdapter = new ArrayAdapter<String>(activity,R.layout.custom_spinner_item,districtNameList);
        distSpinner.setAdapter(distSpinAdapter);

    }

    private void getTehsilsByDistrictMethod(final String districtId){
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        tehsilNameList = new ArrayList<>();
        tehsilIdsList = new ArrayList<>();
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
                                    int i=0;
                                    tehsilIdsList.add("Select Tehsil");
                                    tehsilNameList.add("Select Tehsil");
                                    while (tehsilIdsList.size() != tehsilsObject.length()){
                                        i++;
                                        if (!tehsilsObject.isNull(""+i)){
                                            JSONObject innerObj = tehsilsObject.getJSONObject(""+i);
                                            if (innerObj.length()>0){
                                                tehsilIdsList.add(innerObj.getString("taluka_id"));
                                                tehsilNameList.add(innerObj.getString("taluka_name"));
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        progressDialog.dismiss();
                        setTehsilAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setTehsilAdapter();
                        new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null);
                        progressDialog.dismiss();
                    }

                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                setTehsilAdapter();
                new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
            }
        });
    }

    private void setTehsilAdapter(){
        if (tehsilIdsList.size() ==0){
            tehsilIdsList.add("Select Tehsil");
        }
        if (tehsilNameList.size() == 0){
            tehsilNameList.add("Select Tehsil");
        }

        tehsilSpinAdapter = new ArrayAdapter<String >(activity,R.layout.custom_spinner_item,tehsilNameList);
        tehsilSpinner.setAdapter(tehsilSpinAdapter);

    }
    private void registerUser(UserModel userModel, String selectedLanguage){
        progressDialog.setMessage("Adding User...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new RegisterUserService(activity).registerUser(userModel, selectedLanguage,"Other",null, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        String data = response.getString("data");

                        if (statusDescription.length()>0
                                && statusDescription.equalsIgnoreCase("Success")){

                            JSONObject object = response.getJSONObject("data");
                            if(object!=null && object.length()>0){
                               if(!object.isNull("other_public_user_id") &&!object.getString("other_public_user_id").equalsIgnoreCase("")){
                                   ucIdString = object.getString("other_public_user_id");
                               }
                               if(!object.isNull("user_type") && !object.getString("user_type").equalsIgnoreCase("")){
                                   userType = object.getString("user_type");
                               }
                               session.addOtherUserInfo(ucIdString,userType);
                            }
                            commonCode.showErrorORSuccessAlert(activity, "success", "User information added Successfully", getActivity().getSupportFragmentManager(), null);
                            //CommonCode.updateDisplay(new PreviousAssessmentFragment(),getActivity().getSupportFragmentManager());

                           // new CommonCode(activity).showErrorORSuccessAlert(activity,"success","Registration Successful! Enter OTP for verification",getActivity().getSupportFragmentManager(),bundle);
                        }else if(statusDescription.length()>0
                                && statusDescription.equalsIgnoreCase("Record already Exists")){
                            new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Record already Exists",getActivity().getSupportFragmentManager(),null);
                        }
                        else{
                            new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null);
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();

                        progressDialog.dismiss();
                        new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null);
                    }

                }

            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
            }
        });
    }

    private Boolean isDataValid(){
        Boolean isName = true, isMobileNo = true, isCnic = true, isAge = true, isAddress = true, isDistrict = true, isTehsil=true;
        if(nameString.trim().length() == 0) {
            tvNameError.setText("Name is Required");
            tvNameError.setVisibility(View.VISIBLE);
            isName = false;
        }
        if (mobileNoString.trim().length() == 0){
            tvMobileNoError.setText("Mobile # is Required");
            tvMobileNoError.setVisibility(View.VISIBLE);
            isMobileNo = false;
        }else if (mobileNoString.length()<13){
            tvMobileNoError.setText("Mobile # is Invalid");
            tvMobileNoError.setVisibility(View.VISIBLE);
            isMobileNo = false;
        }
        if (ageString.trim().length() == 0){
            tvAgeError.setText("Age is Required");
            tvAgeError.setVisibility(View.VISIBLE);
            isAge = false;
        }
        if(ageString.trim().equalsIgnoreCase("0")){
            tvAgeError.setText("Age can't be 0");
            tvAgeError.setVisibility(View.VISIBLE);
            isAge = false;
        }
        if (districtIdString.trim().length() == 0){
            tvDistrictError.setText("Please select District");
            tvDistrictError.setVisibility(View.VISIBLE);
            isDistrict = false;
        }
        if (tehsilIdString.length() == 0){
            tvTehsilError.setText("Please select Tehsil");
            tvTehsilError.setVisibility(View.VISIBLE);
            isTehsil = false;
        }
        if (addressString.trim().length() == 0){
            tvAddressError.setText("Address is Required");
            tvAddressError.setVisibility(View.VISIBLE);
            isAddress = false;
        }
        /*if (cnicString.trim().length() == 0){
            tvCnicError.setText("CNIC is Required");
            tvCnicError.setVisibility(View.VISIBLE);
            isCnic = false;
        }else if (cnicString.length()<14){
            tvCnicError.setText("CNIC is Invalid");
            tvCnicError.setVisibility(View.VISIBLE);
            isCnic = false;
        }*/

        return (isName && isMobileNo && isAge && isDistrict &&
                isTehsil && isAddress );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                checkLocation();
                startLocationUpdates();
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if (location == null) {
                    startLocationUpdates();
                }
                if (location != null) {
                    latitude = ("" + location.getLatitude());
                    longitude = ("" + location.getLongitude());
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(activity, "You do not have permission for location.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private boolean isLocationEnabled() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private boolean checkLocation() {

        if (!isLocationEnabled())
            showLocationAlert("Enable location","Location is not enabled. Do you want to Turn on Location?"
                    ,"Yes","No");
        return isLocationEnabled();
    }

    private void showLocationAlert(String title,String message,String positiveBtn,String negativeBtn) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity, R.style.AlertDialogTheme);

        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(myIntent, RequestCode.LOCATION_ENABLED_CODE);


                    }
                })
                .setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        paramDialogInterface.cancel();
                        progressDialog.dismiss();

                    }
                });
        dialog.show();
    }

    protected void startLocationUpdates() {
        // Create the location request

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RequestCode.LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, this);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RequestCode.LOCATION_ENABLED_CODE){

            try {
                int locationMode = Settings.Secure.getInt(activity.getContentResolver(),Settings.Secure.LOCATION_MODE);
                if(locationMode!=0 && locationMode!=LOCATION_MODE_HIGH_ACCURACY){
                    showLocationAlert("Enable high accuracy mode","Enable high accuracy mode from your mobile settings to find location faster"
                            ,"OK","Cancel");
                }


            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, RequestCode.LOCATION_REQUEST_CODE);


        } else {
            checkLocation();
            startLocationUpdates();
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location == null) {

                startLocationUpdates();

            }

            if (location != null) {
                progressDialog.setMessage("Fetching Location...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                //if(CommonCode.isFormattingApply(location.getLatitude())){
                latitude = (""+location.getLatitude());
                /*}
                else{
                    tvLatitudeValue.setText("" + location.getLatitude());
                }
                if(CommonCode.isFormattingApply(location.getLongitude())){*/
                longitude = (""+location.getLongitude());
                /*}
                else{
                    tvLongitudeValue.setText("" + location.getLongitude());
                }

                setMarker(location.getLatitude(), location.getLongitude());*/
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            progressDialog.setMessage("Fetching Location...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            //if(CommonCode.isFormattingApply(location.getLatitude())){
            latitude = (""+location.getLatitude());
            /*}
            else{
                tvLatitudeValue.setText("" + location.getLatitude());
            }
            if(CommonCode.isFormattingApply(location.getLongitude())){*/
            longitude = (""+location.getLongitude());
            /*}
            else{
                tvLongitudeValue.setText("" + location.getLongitude());
            }
            setMarker(location.getLatitude(), location.getLongitude());*/
            progressDialog.dismiss();

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    // Disconnect googleApiClient
    protected void disconnect(){
        if(googleApiClient!=null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
        progressDialog.dismiss();
    }

    // Added By Hina Hussain on 31-March-2020
    @SuppressLint("ClickableViewAccessibility")
    private void addTouchEventsOnSpinner() {
        distSpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    commonCode.hideKeyboard(v);
                    hideCustomKeyboard();
                    if(edAge.hasFocus() || edAddress.hasFocus()){
                        edAddress.clearFocus();
                        edAge.clearFocus();
                    }
                }
                return false;
            }
        });

        tehsilSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    commonCode.hideKeyboard(v);
                    hideCustomKeyboard();
                    if(edAge.hasFocus() || edAddress.hasFocus()){
                        edAddress.clearFocus();
                        edAge.clearFocus();
                    }
                }
                return false;
            }
        });

        spGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    commonCode.hideKeyboard(v);
                    hideCustomKeyboard();
                    if(edAge.hasFocus() || edAddress.hasFocus()){
                        edAddress.clearFocus();
                        edAge.clearFocus();
                    }

                }
                return false;
            }
        });
    }

    private void showKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null)
        {
            inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(),0);
            inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        mainScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect measureRect = new Rect();
                mainScrollView.getWindowVisibleDisplayFrame(measureRect);
                // measureRect.bottom is the position above soft keypad
                int keypadHeight = mainScrollView.getRootView().getHeight() - measureRect.bottom;

                if (keypadHeight > 0) {
                    // keyboard is opened
                    System.out.println("=====keyboard open");


                    if(edCnic.hasFocus()){
                        NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.GONE);
                        System.out.println("=====edName::"+edCnic.getTop()+"----"+edCnic.getHeight()+" --- "+edCnic.getTop());
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewSpacing.getLayoutParams();
                        layoutParams.height = 20+(edCnic.getHeight()/2);
                        mainScrollView.smoothScrollTo(0,viewSpacing.getBottom());

                    }

                }
            }
        });
    }
}
