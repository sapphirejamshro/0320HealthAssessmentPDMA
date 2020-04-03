package com.sapphire.HealthAssessmentPDMA.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.helper.BackPressAwareAutoTextview;
import com.sapphire.HealthAssessmentPDMA.helper.BackPressAwareEdittext;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.CustomTypefaceSpan;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.LocationService;
import com.sapphire.HealthAssessmentPDMA.webService.SelfAssessmentQuestionService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class OtherAssessmentFragment extends Fragment {

    private View view,viewEmptyNo,spacingViewFlighLayout,spacingViewPersonInfoLayout,viewEmptySubmit;
    private Activity activity;
    private TextView tvQuestion,tvEngMsg,tvCountry,tvFlightNo,tvPassportNo,tvCountryError,tvFlightNoError,tvPassportNoError,
            tvNameError,tvMobNoError,tvName,tvMobileNo,tvQuestionNo,tvOtherDiseaseError;
    private Button btnYes,btnNo,btnSubmit,btnBack,btnNextFlight,btnNextPersonInfo;
    private CardView cardViewSubmitButton,cardViewYesButton,cardViewNoButton,cardViewBackButton, cardViewOtherDiseasePSF;
    private View flightHistoryView,personInfoView;
    private ScrollView mainScrollView;
    private EditText commonEditText;
    private BackPressAwareEdittext edPassportNo,edName,edFlightNo,edMobileNo,edOtherDisease;
    private BackPressAwareAutoTextview spCountry;
    private ArrayAdapter<String> countryAdapter;
    private int msgCounter = 0;
    private List<String> questionList = null,answersList=null,countryNameList = null,countryIdList,optionListQuest8 = null;
    private String selectedLanguage = "",yesBtnText = "",noBtnText = "";
    private ProgressDialog progressDialog;
    private CommonCode commonCode;
    private LinearLayout linearLayoutAnswers ;
    private int noCount=0,yesCount=0;
    private Boolean isForthQuesYesClick = false,isSpinnerCountryErrOccur = false,isFifthQuesYesClick = false;
    private String flightNoString="",passportNoString = "",countryIdString = "",nameString = "",mobileNoString = "";
    private HashMap<String, String> questionsResultHashMap;
    private TextWatcher mobileTextWatcher;
    //For Keyboard
    private KeyboardView keyboardview;
    private Keyboard keyboardQwertyFirst, keyboardQwertySecond, keyboardNumbers,
            keyboardPunctuationsFirst, keyboardPunctuationsSecond;
    private int count=0;
    private View viewKeyboard,viewKeyboardPerson,viewEmptyBack;
    String countryString="", otherDiseaseName="";
    private Boolean isBackButtonClicked=false,isFifthQuesYes=false;
    private AutoCompleteTextView commonAutoCompleteTv;
    private HashMap<String,String> countryMap;
    private int screenHeight = 0;

    public OtherAssessmentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_other_assessment, container, false);
        activity = getActivity();
        msgCounter = 0;
        NavigationDrawerActivity.tvHeading.setText("OTHER ASSESSMENT");
        init();
        countryString = "";
        countryMap = new HashMap<>();
        isForthQuesYesClick = false;
        isBackButtonClicked = false;
        isFifthQuesYes = false;
        screenHeight =  getHeight();


        linearLayoutAnswers.setVisibility(View.GONE);
        // Add for testing
        cardViewBackButton.setVisibility(View.GONE);
        cardViewNoButton.setVisibility(View.GONE);
        cardViewYesButton.setVisibility(View.GONE);
        flightHistoryView.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(activity);
        questionList = new ArrayList<>();
        answersList = new ArrayList<>();
        countryNameList = new ArrayList<>();
        countryIdList = new ArrayList<>();
        questionsResultHashMap = new HashMap<>();
        optionListQuest8 = new ArrayList<>();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        commonCode = new CommonCode(activity);
        if(commonCode.isNetworkAvailable()){
            getAssessmentQuestions();
        }
        else{
            commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());

        }
        if(selectedLanguage.equalsIgnoreCase("Urdu")){
            resSetBackButtonMargin();
        }
        buttonClickEvent();
        addAutoTextviewListener();
        addTextWatcher();
        addFocusChangeListener();
        addBackPressAwareListener();
        scrollViewListener();
        setOnEditorActionListeners();
        return view;
    }


    private void buttonClickEvent() {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEmptyNo.setVisibility(View.GONE);
                cardViewBackButton.setVisibility(View.VISIBLE);

                hideCustomKeyboard();
                commonCode.hideKeyboard(v);
                // for Question No 04
                if (msgCounter == 3) {
                    // flightHistoryView.setVisibility(View.VISIBLE);
                    if(!isForthQuesYesClick){
                        // if first time click
                        countryString = "";
                        edFlightNo.setText("");
                        edPassportNo.setText("");
                        isForthQuesYesClick = true;
                        viewEmptyNo.setVisibility(View.GONE);
                        if(commonCode.isNetworkAvailable()){
                            getAllCountries();
                        }
                        else{
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
                        }
                    }

                }
                // for Question no 5
                else if(msgCounter==4){
                    if (!isFifthQuesYes) {
                        edName.setText("");
                        edMobileNo.setText("");
                        isFifthQuesYes = true;
                        personInfoView.setVisibility(View.VISIBLE);
                        viewEmptyNo.setVisibility(View.GONE);
                        setCustomFonts();
                    }
                }
                else {
                    viewEmptyNo.setVisibility(View.VISIBLE);
                    msgCounter++;
                    if (msgCounter < questionList.size()) {
                        //cardViewBackButton.setVisibility(View.VISIBLE);


                        questionsResultHashMap.put(String.valueOf(msgCounter), btnYes.getText().toString());
                        tvQuestion.setText((msgCounter + 1) + ")  " + questionList.get(msgCounter));
                        yesCount++;
                        setAnswersOptions();
                        mainScrollView.smoothScrollTo(0,0);
                        setQuestionNum((msgCounter + 1),questionList.size());
                        if (msgCounter == 3 && isForthQuesYesClick && isBackButtonClicked){
                            setCountryAdapter();
                        }
                        if (msgCounter == 4 && isFifthQuesYes && isBackButtonClicked){
                            personInfoView.setVisibility(View.VISIBLE);
                            viewEmptyNo.setVisibility(View.GONE);
                            setCustomFonts();
                        }
                    } else {
                        if(commonCode.isNetworkAvailable()){
                            addAssessmentData();
                        }
                        else{
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
                        }
                    }
                }
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCustomKeyboard();
                commonCode.hideKeyboard(v);
                cardViewBackButton.setVisibility(View.VISIBLE);


                msgCounter++;
                if (msgCounter == 4){
                    isForthQuesYesClick = false;
                }
                if (msgCounter == 5){
                    isFifthQuesYes = false;
                }
                if(msgCounter<questionList.size()){
                    //cardViewBackButton.setVisibility(View.VISIBLE);
                    questionsResultHashMap.put(String.valueOf(msgCounter),btnNo.getText().toString());
                    flightHistoryView.setVisibility(View.GONE);
                    personInfoView.setVisibility(View.GONE);
                    viewEmptyNo.setVisibility(View.VISIBLE);

                    tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                    setAnswersOptions();
                    setQuestionNum((msgCounter + 1),questionList.size());
                    if (msgCounter == 3 && isForthQuesYesClick && isBackButtonClicked){
                        setCountryAdapter();
                    }
                    if (msgCounter == 4 && isFifthQuesYes && isBackButtonClicked){
                        personInfoView.setVisibility(View.VISIBLE);
                        viewEmptyNo.setVisibility(View.GONE);
                        setCustomFonts();
                    }
                    noCount++;
                }
                else{
                    if(commonCode.isNetworkAvailable()){
                        addAssessmentData();
                    }
                    else{
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCustomKeyboard();
                commonCode.hideKeyboard(v);
                if(optionListQuest8==null || optionListQuest8.size()==0){
                    tvEngMsg.setText("Select at least one option from following");
                    tvEngMsg.setVisibility(View.VISIBLE);
                }else if(cardViewOtherDiseasePSF.getVisibility() == View.VISIBLE &&
                        otherDiseaseName.trim().length()==0 ){
                    tvOtherDiseaseError.setVisibility(View.VISIBLE);
                }else if (commonCode.isNetworkAvailable()) {
                    hideCustomKeyboard();
                    commonCode.hideKeyboard(v);
                    if(edOtherDisease != null && edOtherDisease.hasFocus()){
                        edOtherDisease.clearFocus();
                    }
                    addAssessmentData();
                }
                else{
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null);
                }


                /*if (yesCount>noCount){
                    PublicSurveyResultFragment fragment =  new PublicSurveyResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("surveyResult","PositiveResult");
                    fragment.setArguments(bundle);
                    CommonCode.updateDisplay(fragment,getActivity().getSupportFragmentManager());

                }else{
                    PublicSurveyResultFragment fragment =  new PublicSurveyResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("surveyResult","NegativeResult");
                    fragment.setArguments(bundle);
                    CommonCode.updateDisplay(fragment,getActivity().getSupportFragmentManager());
                }*/
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 resSetBackButtonMargin();
                tvEngMsg.setVisibility(View.GONE);
                if (cardViewOtherDiseasePSF.getVisibility() == View.VISIBLE){
                    cardViewOtherDiseasePSF.setVisibility(View.GONE);
                    tvOtherDiseaseError.setVisibility(View.GONE);
                }
                msgCounter--;
                if(msgCounter<questionList.size() && msgCounter>=0){
                    cardViewBackButton.setVisibility(View.VISIBLE);
                    tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                    //yesCount++;
                    if( msgCounter == 5){
                        isBackButtonClicked = true;
                    }
                    setAnswersOptions();
                    setQuestionNum((msgCounter + 1),questionList.size());
                }
                if (msgCounter == 3 || msgCounter == 2){
                    isBackButtonClicked = true;
                }
                if (msgCounter != 3 && msgCounter != 4){
                    flightHistoryView.setVisibility(View.GONE);
                    personInfoView.setVisibility(View.GONE);
                }else if (msgCounter==4){
                    if (isFifthQuesYes) {
                        personInfoView.setVisibility(View.VISIBLE);
                        viewEmptyNo.setVisibility(View.GONE);
                        setCustomFonts();
                    }
                }else {
                    //if (flightHistoryView.getVisibility() == View.VISIBLE){
                    if (isForthQuesYesClick) {
                        setCountryAdapter();
                    }
                    //flightHistoryView.setVisibility(View.GONE);
                    personInfoView.setVisibility(View.GONE);

                    // }
                }
                if(msgCounter==0){
                    cardViewBackButton.setVisibility(View.GONE);
                    viewEmptyNo.setVisibility(View.VISIBLE);
                }

                if(edOtherDisease != null && edOtherDisease.hasFocus()){
                    edOtherDisease.clearFocus();
                }
                /*if(msgCounter<questionList.size() && msgCounter>=0){
                    cardViewBackButton.setVisibility(View.VISIBLE);
                    tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                    tvEngMsg.setVisibility(View.GONE);
                    //yesCount++;
                    setAnswersOptions();
                    setQuestionNum((msgCounter + 1),questionList.size());
                    // to check for Question 4
                    if(msgCounter==3 && isForthQuesYesClick){
                        flightHistoryView.setVisibility(View.VISIBLE);
                        edFlightNo.setText(flightNoString);
                        edPassportNo.setText(passportNoString);
                        if(countryIdString!=null && !countryIdString.equalsIgnoreCase("")){
                            int pos = countryAdapter.getPosition(countryIdString);
                            System.out.println("=====pos::"+pos);
                            spCountry.setSelection(pos);
                        }
                    }
                    // to check for Question 5
                    else if(msgCounter==4 && isFifthQuesYesClick){
                        personInfoView.setVisibility(View.VISIBLE);
                        edName.setText(nameString);
                        edMobileNo.setText(mobileNoString);
                    }

                    else{
                        // hide both views
                        flightHistoryView.setVisibility(View.GONE);
                        personInfoView.setVisibility(View.GONE);

                    }

                }
                if(msgCounter==0){
                    cardViewBackButton.setVisibility(View.GONE);
                    flightHistoryView.setVisibility(View.GONE);
                    personInfoView.setVisibility(View.GONE);
                }*/

            }
        });

        btnNextFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCustomKeyboard();
                commonCode.hideKeyboard(v);
                if(validateData()) {

                    msgCounter++;
                    if (msgCounter < questionList.size()) {
                        flightHistoryView.setVisibility(View.GONE);
                        mainScrollView.smoothScrollTo(0, 0);
                        tvQuestion.setText((msgCounter + 1) + ")  " + questionList.get(msgCounter));
                        setAnswersOptions();
                        setQuestionNum((msgCounter + 1),questionList.size());
                        yesCount++;
                        if (msgCounter == 4 && isFifthQuesYes && isBackButtonClicked){
                            personInfoView.setVisibility(View.VISIBLE);
                            viewEmptyNo.setVisibility(View.GONE);
                            setCustomFonts();
                        }
                        questionsResultHashMap.put(String.valueOf(msgCounter), btnYes.getText().toString());
                        /*if (spCountry.getText().toString().trim().length() != 0) {
                            countryString = spCountry.getText().toString();
                            countryAdapter = new ArrayAdapter<>(activity,R.layout.country_spinner_item,countryNameList);
                            spCountry.setAdapter(countryAdapter);
                            int countryPos = countryAdapter.getPosition(countryString);

                            System.out.println("=======================pos "+countryPos);
                            if (countryPos != -1 && countryNameList.size()>0) {
                                countryIdString = countryIdList.get(countryPos);
                                System.out.println("=======================id "+countryPos);
                            }else {

                            }


                        }*/
                        passportNoString = edPassportNo.getText().toString();
                        flightNoString = edFlightNo.getText().toString();

                    }
                }
            }
        });

        btnNextPersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCustomKeyboard();
                commonCode.hideKeyboard(v);
                if(personInfoValidate()){
                    msgCounter++;
                    if (msgCounter < questionList.size()) {
                        personInfoView.setVisibility(View.GONE);
                        mainScrollView.smoothScrollTo(0, 0);
                        tvQuestion.setText((msgCounter + 1) + ")  " + questionList.get(msgCounter));
                        setAnswersOptions();
                        setQuestionNum((msgCounter + 1),questionList.size());
                        yesCount++;
                        questionsResultHashMap.put(String.valueOf(msgCounter), btnYes.getText().toString());
                        nameString = edName.getText().toString().trim();
                        mobileNoString = edMobileNo.getText().toString().trim();

                    }
                }
            }
        });
    }

    private void setOnEditorActionListeners(){
        edMobileNo.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                edMobileNo.clearFocus();
                return false;
            }
        });
    }

    private Boolean validateData() {
        Boolean isValidCity = true, isValidFlightNo = true,isValidPassportNo=true;
        if(spCountry.getText().toString().trim().length()==0){
            tvCountryError.setVisibility(View.VISIBLE);
            tvCountryError.setText("Please select Country.");
            isValidCity = false;
            isSpinnerCountryErrOccur = true;
        }else if (tvCountryError.getVisibility() == View.VISIBLE){
            tvCountryError.setVisibility(View.VISIBLE);
            tvCountryError.setText("Country name does not exist.");
            isValidCity = false;
            isSpinnerCountryErrOccur = true;
        }
        /*if(edFlightNo.getText().toString().trim()==null || edFlightNo.getText().toString().trim().equalsIgnoreCase("")
                || edFlightNo.getText().toString().trim().isEmpty()){
            tvFlightNoError.setVisibility(View.VISIBLE);
            isValidFlightNo  = false;
        }
        if(edPassportNo.getText().toString().trim()==null || edPassportNo.getText().toString().trim().equalsIgnoreCase("")
                || edPassportNo.getText().toString().trim().isEmpty()){
            tvPassportNoError.setVisibility(View.VISIBLE);
            isValidPassportNo  = false;
        }*/
        return (isValidCity);
    }


    private void init() {
        tvEngMsg = view.findViewById(R.id.tv_englishuMsg_OtherAF);
        tvQuestionNo = view.findViewById(R.id.tvQuestionNumber_other_assessment);
        tvQuestion = view.findViewById(R.id.tv_urduMsg_OtherAF);
        btnYes = view.findViewById(R.id.btn_yes_OtherAF);
        btnNo = view.findViewById(R.id.btn_no_OtherAF);
        linearLayoutAnswers = view.findViewById(R.id.linearLayoutAnswers);
        btnSubmit = view.findViewById(R.id.btnSubmitOtherAFrag);
        cardViewSubmitButton = view.findViewById(R.id.cardViewSubmitButtonOtherAFrag);
        cardViewYesButton = view.findViewById(R.id.cardViewYesButton_OtherAF);
        cardViewNoButton = view.findViewById(R.id.cardViewNoButton_OtherAF);
        cardViewBackButton = view.findViewById(R.id.cardViewBackButton_OtherAF);
        btnBack = view.findViewById(R.id.btn_back_OtherAF);
        flightHistoryView = view.findViewById(R.id.flightLayout);
        spCountry = view.findViewById(R.id.spin_dist_user_info_frag);
        edFlightNo = view.findViewById(R.id.edFlightNo);
        edPassportNo = view.findViewById(R.id.edPassportNo);
        btnNextFlight = view.findViewById(R.id.btn_next_flight_HL);
        mainScrollView = view.findViewById(R.id.scrollView_OtherAF);
        tvCountry = view.findViewById(R.id.tv_country_flight_HL);
        tvFlightNo = view.findViewById(R.id.tv_flightNo_flight_HL);
        tvPassportNo = view.findViewById(R.id.tv_passportNo_flight_HL);
        tvCountryError  = view.findViewById(R.id.tv_countryError_flight_HL);
        tvFlightNoError = view.findViewById(R.id.tv_flightNoError_flight_HL);
        tvPassportNoError = view.findViewById(R.id.tv_passportNoError_flight_HL);
        personInfoView = view.findViewById(R.id.personInfoLayout);
        spacingViewFlighLayout = view.findViewById(R.id.view_spacing_flight_history_layout);
        spacingViewPersonInfoLayout = view.findViewById(R.id.view_spacing_person_contact_info_layout);
        edName = view.findViewById(R.id.edName);
        edMobileNo =  view.findViewById(R.id.edMobileNo);
        btnNextPersonInfo = view.findViewById(R.id.btn_next_person_CILayout);
        tvMobNoError = view.findViewById(R.id.tv_MobileError_person_CILayout);
        tvNameError =  view.findViewById(R.id.tv_nameError_person_CILayout);
        tvName = view.findViewById(R.id.tv_name_person_CILayout);
        tvMobileNo = view.findViewById(R.id.tv_mobileNo_person_CILayout);
        viewEmptyNo = view.findViewById(R.id.viewEmptyNo);
        //For Keyboard
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        keyboardview = NavigationDrawerActivity.keyboardview;
        viewKeyboard = view.findViewById(R.id.viewKeyboard);
        viewKeyboardPerson= view.findViewById(R.id.viewKeyboardPerson);
        edOtherDisease = view.findViewById(R.id.edOtherDiseasesOAF);
        cardViewOtherDiseasePSF = view.findViewById(R.id.cardViewOtherDiseasePSFOAF);
        viewEmptySubmit = view.findViewById(R.id.viewEmptySubmit);
        tvOtherDiseaseError = view.findViewById(R.id.tvOtherDiseaseErrorOAFrag);
    }


    private void setAnswersOptions(){
        String answers[] = answersList.get(msgCounter).split(",");
        if (answers.length == 2){
            resSetBackButtonMargin();
            for (int i=0; i<answers.length;i++){
                cardViewYesButton.setVisibility(View.VISIBLE);
                cardViewNoButton.setVisibility(View.VISIBLE);
                linearLayoutAnswers.setVisibility(View.GONE);
                cardViewSubmitButton.setVisibility(View.GONE);
                btnYes.setText(answers[0]);
                btnNo.setText(answers[1]);

            }
        }else if (answers.length > 0){
            cardViewYesButton.setVisibility(View.GONE);
            cardViewNoButton.setVisibility(View.GONE);

            linearLayoutAnswers.setVisibility(View.VISIBLE);
            cardViewSubmitButton.setVisibility(View.VISIBLE);
            linearLayoutAnswers.removeAllViews();
            Boolean isTrue=false;

            setBackButtonMargin();
            for (int i=0; i<answers.length;i++){

                final CheckBox cb = new CheckBox(activity);

                edOtherDisease.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().trim().length()>0){
                            otherDiseaseName = s.toString();
                            tvOtherDiseaseError.setVisibility(View.GONE);

                        }else {
//                            tvOtherDiseaseError.setVisibility(View.VISIBLE);
                            otherDiseaseName="";
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (selectedLanguage.equalsIgnoreCase("Urdu")) {
                        cb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        Typeface fontUrdu = Typeface.createFromAsset(getActivity().getAssets(),"notonastaliqurdu_regular.ttf");
                        cb.setTypeface(fontUrdu);

                            edOtherDisease.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            //edOtherDisease.setTypeface(fontUrdu);
                    }else if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
                        cb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        Typeface fontSindhi = Typeface.createFromAsset(getActivity().getAssets(),"sindhi_fonts.ttf");
                        cb.setTypeface(fontSindhi);

                            edOtherDisease.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                           // edOtherDisease.setTypeface(fontSindhi);
                    }else {
                        Typeface fontEng = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_regular.ttf");
                        cb.setTypeface(fontEng);
                        cb.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            edOtherDisease.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            //edOtherDisease.setTypeface(fontEng);

                    }
                }
                cb.setId((i+1));
                cb.setTextSize(20f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.appColorBlack)));
                }
                cb.setHighlightColor(getResources().getColor(R.color.appColorBlack));
                cb.setText("   "+answers[i]);

                linearLayoutAnswers.addView(cb);


                if (isBackButtonClicked){
                    if (optionListQuest8.size()>0){
                        for (int s=0; s<optionListQuest8.size(); s++ ){
                            if (cb.getText().toString().trim().equalsIgnoreCase(optionListQuest8.get(s))){
                                cb.setChecked(true);
                            }

                        }
                        if ((cb.getText().toString().trim().equalsIgnoreCase("Other")
                                || cb.getText().toString().trim().equalsIgnoreCase("کوئی اور بيماری") ||
                                cb.getText().toString().trim().equalsIgnoreCase("ٻي ڪا بيماري")) && cb.isChecked() ) {
                            isTrue = true;
                            if (edOtherDisease.getVisibility() == View.VISIBLE && edOtherDisease.getText().toString().trim().length()==0){
                                tvOtherDiseaseError.setVisibility(View.VISIBLE);
                            }
                            cardViewOtherDiseasePSF.setVisibility(View.VISIBLE);
                            setSubmitButtonMargin();
                            if (selectedLanguage.equalsIgnoreCase("en")) {
                                disableCheckBoxex(linearLayoutAnswers, "Other");
                            }else if (selectedLanguage.equalsIgnoreCase("urdu")){
                                disableCheckBoxex(linearLayoutAnswers,"کوئی اور بيماری");
                            }else if (selectedLanguage.equalsIgnoreCase("sindhi")){
                                disableCheckBoxex(linearLayoutAnswers,"ٻي ڪا بيماري");
                            }

                            //break;
                        }else  if ((cb.getText().toString().trim().equalsIgnoreCase("None of the Above")
                                || cb.getText().toString().trim().equalsIgnoreCase("کوئي بيماری نہیں")
                                || cb.getText().toString().trim().equalsIgnoreCase("بيماري ڪونهي")) && cb.isChecked() ) {
                            isTrue = true;
                            if (selectedLanguage.equalsIgnoreCase("en")) {
                                disableCheckBoxex(linearLayoutAnswers, "None of the Above");
                            }else if (selectedLanguage.equalsIgnoreCase("urdu")){
                                disableCheckBoxex(linearLayoutAnswers,"کوئي بيماری نہیں");
                            }else if (selectedLanguage.equalsIgnoreCase("sindhi")) {
                                disableCheckBoxex(linearLayoutAnswers, "بيماري ڪونهي");
                            }

                        }else if (isTrue){
                            cb.setEnabled(false);
                            isTrue = false;

                        }
                    }
                }

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //questionsResultHashMap.put(String.valueOf(msgCounter),cb.getText().toString());
                        if(cb.isChecked()){
                            tvEngMsg.setVisibility(View.GONE);

                            if(cb.getText().toString().trim().equalsIgnoreCase("Other") ||
                                    cb.getText().toString().trim().equalsIgnoreCase("کوئی اور بيماری") ||
                                    cb.getText().toString().trim().equalsIgnoreCase("ٻي ڪا بيماري") ||
                                    cb.getText().toString().trim().equalsIgnoreCase("None Of the Above") ||
                                    cb.getText().toString().trim().equalsIgnoreCase("کوئي بيماری نہیں")
                                    || cb.getText().toString().trim().equalsIgnoreCase("بيماري ڪونهي")
                            ){
                                optionListQuest8.clear();
                            }

                            if (!optionListQuest8.contains(cb.getText().toString())) {
                                optionListQuest8.add(cb.getText().toString().trim());
                            }
                            if (cb.getText().toString().trim().equalsIgnoreCase("Other")
                                    || cb.getText().toString().trim().equalsIgnoreCase("کوئی اور بيماری")
                                    || cb.getText().toString().trim().equalsIgnoreCase("ٻي ڪا بيماري")){

                                if (selectedLanguage.equalsIgnoreCase("en")) {
                                    disableCheckBoxex(linearLayoutAnswers, "Other");
                                }else if (selectedLanguage.equalsIgnoreCase("urdu")) {
                                    disableCheckBoxex(linearLayoutAnswers, "کوئی اور بيماری");
                                }else if (selectedLanguage.equalsIgnoreCase("sindhi")){
                                    disableCheckBoxex(linearLayoutAnswers,"ٻي ڪا بيماري");
                                }
                                cardViewOtherDiseasePSF.setVisibility(View.VISIBLE);
                                setSubmitButtonMargin();
                            }else if (cb.getText().toString().trim().equalsIgnoreCase("None Of the Above") ||
                                    cb.getText().toString().trim().equalsIgnoreCase("کوئي بيماری نہیں")
                                    || cb.getText().toString().trim().equalsIgnoreCase("بيماري ڪونهي")){
                                if (selectedLanguage.equalsIgnoreCase("en")) {
                                    disableCheckBoxex(linearLayoutAnswers, "None of the Above");
                                }else if (selectedLanguage.equalsIgnoreCase("urdu")) {
                                    disableCheckBoxex(linearLayoutAnswers, "کوئي بيماری نہیں");
                                }else if (selectedLanguage.equalsIgnoreCase("sindhi")) {
                                    disableCheckBoxex(linearLayoutAnswers, "بيماري ڪونهي");
                                }
                            }
                        }
                        else{
                            cardViewOtherDiseasePSF.setVisibility(View.GONE);
                            resSetSubmitButtonMargin();
                            otherDiseaseName="";
                            edOtherDisease.setText("");
                            hideCustomKeyboard();
                            tvOtherDiseaseError.setVisibility(View.GONE);
                            optionListQuest8.remove(cb.getText().toString().trim());
                            for (int j=0;j<linearLayoutAnswers.getChildCount();j++) {
                                View nextChild = linearLayoutAnswers.getChildAt(j);
                                if (nextChild instanceof CheckBox) {
                                    CheckBox check = (CheckBox) nextChild;
                                    check.setEnabled(true);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private void getAssessmentQuestions() {
        progressDialog.setMessage("Fetching question...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new SelfAssessmentQuestionService(activity).getAssessmentQuestion(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response!=null){
                    try {
                        if(!response.isNull("data") && response.getString("data")!=null && !response.getString("data").equals("")){
                            JSONObject dataObj = new JSONObject(response.getString("data"));
                            if(dataObj!=null && dataObj.length()>0){
                                int ans=0,quesCount=0;
                                for(int i=1;i<=dataObj.length();i++){
                                    quesCount++;
                                    if(!dataObj.isNull(String.valueOf((quesCount)))){
                                        JSONObject questionObj = dataObj.getJSONObject(String.valueOf((quesCount)));
                                        JSONObject optionObject = null;
                                        if(!questionObj.isNull("options")){
                                            optionObject = questionObj.getJSONObject("options");
                                        }

                                        if(selectedLanguage.equalsIgnoreCase("en")){
                                            if(!questionObj.isNull("question_en") && !questionObj.getString("question_en").equals("")){
                                                questionList.add(questionObj.getString("question_en"));
                                            }
                                            // to get Option Text
                                            if(optionObject!=null){

                                                StringBuilder answer = new StringBuilder();
                                                Boolean isFirst=true;
                                                for (int j=0; j<optionObject.length(); j++){
                                                    ans++;
                                                    // for Yes
                                                    if (!optionObject.isNull("" + ans)) {
                                                        JSONObject option1 = optionObject.getJSONObject("" + ans);
                                                        if (!option1.isNull("option_en")) {
                                                            if (isFirst) {
                                                                isFirst = false;
                                                                answer.append(option1.getString("option_en"));
                                                            }else {
                                                                answer.append(",").append(option1.getString("option_en"));
                                                            }
                                                        }
                                                    }else{
                                                        j--;

                                                    }
                                                }
                                                isFirst = true;
                                                answersList.add(String.valueOf(answer));
                                            /*if (optionObject.length() == 2) {
                                                ans++;
                                                // for Yes
                                                if (!optionObject.isNull("" + ans)) {
                                                    JSONObject option1 = optionObject.getJSONObject("" + ans);
                                                    if (!option1.isNull("option_en")) {
                                                        yesBtnText = option1.getString("option_en");
                                                    }
                                                }
                                                ans++;
                                                // for No
                                                if (!optionObject.isNull("" + ans)) {
                                                    JSONObject option2 = optionObject.getJSONObject(""+ans);
                                                    if (!option2.isNull("option_en")) {
                                                        noBtnText = option2.getString("option_en");
                                                    }
                                                }
                                            }else {
                                                ans++;
                                                JSONObject option3 = optionObject.getJSONObject(""+ans);
                                                btnYes.setVisibility(View.GONE);
                                                btnNo.setVisibility(View.GONE);
                                                linearLayoutAnswers.setVisibility(View.VISIBLE);
                                                linearLayoutAnswers.removeAllViews();
                                                CheckBox cb = new CheckBox(activity);
                                                cb.setId((ans));
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    cb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.appColorBlack)));
                                                }
                                                cb.setHighlightColor(getResources().getColor(R.color.appColorBlack));
                                                cb.setText(option3.getString("option_en"));

                                                linearLayoutAnswers.addView(cb);
                                            }*/
                                            }
                                        }
                                        else if(selectedLanguage.equalsIgnoreCase("Urdu")){
                                            if(!questionObj.isNull("question_urdu") && !questionObj.getString("question_urdu").equals("")){
                                                questionList.add(questionObj.getString("question_urdu"));
                                            }
                                            // to get Option Text
                                            if(optionObject!=null){

                                                StringBuilder answer = new StringBuilder();
                                                Boolean isFirst=true;
                                                for (int j=0; j<optionObject.length(); j++){
                                                    ans++;
                                                    // for Yes
                                                    if (!optionObject.isNull("" + ans)) {
                                                        JSONObject option1 = optionObject.getJSONObject("" + ans);
                                                        if (!option1.isNull("option_urdu")) {
                                                            if (isFirst) {
                                                                isFirst = false;
                                                                answer.append(option1.getString("option_urdu"));
                                                            }else {
                                                                answer.append(",").append(option1.getString("option_urdu"));
                                                            }
                                                        }
                                                    }else{
                                                        j--;

                                                    }
                                                }
                                                isFirst = true;
                                                answersList.add(String.valueOf(answer));

                                            }
                                        }

                                        else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                                            if(!questionObj.isNull("question_sindhi") && !questionObj.getString("question_sindhi").equals("")){
                                                questionList.add(questionObj.getString("question_sindhi"));

                                            }
                                            // to get Option Text
                                            if(optionObject!=null){
                                                StringBuilder answer = new StringBuilder();
                                                Boolean isFirst=true;
                                                for (int j=0; j<optionObject.length(); j++){
                                                    ans++;
                                                    // for Yes
                                                    if (!optionObject.isNull("" + ans)) {
                                                        JSONObject option1 = optionObject.getJSONObject("" + ans);
                                                        if (!option1.isNull("option_sindhi")) {
                                                            if (isFirst) {
                                                                isFirst = false;
                                                                answer.append(option1.getString("option_sindhi"));
                                                            }else {
                                                                answer.append(",").append(option1.getString("option_sindhi"));
                                                            }
                                                        }
                                                    }else{
                                                        j--;
                                                    }
                                                }
                                                isFirst = true;
                                                answersList.add(String.valueOf(answer));
                                            }
                                        }
                                    }else {
                                        i--;

                                    }

                                } // end of For Loop
                                setDataInTextView();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setDataInTextView() {
        cardViewYesButton.setVisibility(View.VISIBLE);
        cardViewNoButton.setVisibility(View.VISIBLE);
        btnYes.setText(yesBtnText);
        btnNo.setText(noBtnText);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(selectedLanguage.equalsIgnoreCase("en")){
            Typeface fontEng = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_regular.ttf");
            Typeface fontEngBold = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_bold.ttf");
            btnYes.setTypeface(fontEng);
            btnNo.setTypeface(fontEng);
            btnYes.setPadding(0,0,0,0);
            btnNo.setPadding(0,0,0,0);
            if(questionList!=null && questionList.size()>0){
                tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                tvQuestion.setLineSpacing(0.48f,0.68f);

                tvQuestionNo.setTypeface(fontEngBold);
                setAnswersOptions();
                setQuestionNum((msgCounter + 1),questionList.size());

            }
        }

        else if(selectedLanguage.equalsIgnoreCase("Urdu")){
            Typeface fontUrdu = Typeface.createFromAsset(getActivity().getAssets(),"notonastaliqurdu_regular.ttf");
            btnYes.setTypeface(fontUrdu);
            btnNo.setTypeface(fontUrdu);
            btnYes.setIncludeFontPadding(false);
            btnNo.setIncludeFontPadding(false);
            btnYes.setPadding(0,-20,0,20);
            btnNo.setPadding(0,-20,0,20);
            tvQuestionNo.setPadding(0,-10,0,10);

            if(questionList!=null && questionList.size()>0){
                tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                tvQuestion.setTypeface(fontUrdu);
                tvQuestion.setIncludeFontPadding(false);
                tvQuestionNo.setTypeface(fontUrdu);
                tvQuestionNo.setIncludeFontPadding(false);
                setAnswersOptions();
                setQuestionNum((msgCounter + 1),questionList.size());
            }
        }

        if(selectedLanguage.equalsIgnoreCase("Sindhi")){
            Typeface fontSindhi = Typeface.createFromAsset(getActivity().getAssets(),"sindhi_fonts.ttf");
            btnYes.setTypeface(fontSindhi);
            btnNo.setTypeface(fontSindhi);
            tvQuestionNo.setTypeface(fontSindhi);
            tvQuestionNo.setPadding(0,8,0,-20);
            if(questionList!=null && questionList.size()>0){
                tvQuestion.setText((msgCounter+1)+")  "+questionList.get(msgCounter));
                tvQuestion.setTypeface(fontSindhi);
                setAnswersOptions();
                setQuestionNum((msgCounter + 1),questionList.size());
            }
        }

    }

    private void setBackButtonMargin(){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 30, 10, 0);
        params.topToBottom = R.id.cardViewSubmitButtonOtherAFrag;
        //params.bottomToBottom = R.id.innerView_OtherAF;
        cardViewBackButton.setLayoutParams(params);
    }

    private void resSetBackButtonMargin(){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 30, 10, 0);
        params.topToBottom = R.id.cardViewNoButton_OtherAF;
        //params.bottomToBottom = R.id.innerView_OtherAF;
        cardViewBackButton.setLayoutParams(params);
    }
    private void setSubmitButtonMargin(){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 30, 10, 0);
        params.topToBottom = R.id.cardViewOtherDiseasePSFOAF;
        //params.bottomToBottom = R.id.innerView_PSF;
        cardViewSubmitButton.setLayoutParams(params);
    }

    private void resSetSubmitButtonMargin(){
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 30, 10, 0);
        params.topToBottom = R.id.linearLayoutAnswers;
        //params.bottomToBottom = R.id.innerView_PSF;
        cardViewSubmitButton.setLayoutParams(params);
    }

    private void getAllCountries() {
        progressDialog.setMessage("Fetching countries...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new LocationService(activity).getAllCountries(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response!=null){
                    try {
                        if(!response.isNull("data") && !response.getString("data").equalsIgnoreCase("")){
                            JSONObject dataObject = response.getJSONObject("data");
                            if(dataObject!=null && dataObject.length()>0){
                                countryNameList.clear();
                                countryMap.clear();
                                //countryNameList.add("Select Country");
                                for(int i=0;i<dataObject.length();i++){
                                    String key = String.valueOf((i+1));
                                    if(!dataObject.isNull(key) && dataObject.getString(key)!=null && !dataObject.getString(key).equalsIgnoreCase("")){
                                        countryNameList.add(dataObject.getString(key));
                                        countryIdList.add(key);
                                        countryMap.put(dataObject.getString(key),key);
                                    }
                                } // end of for Loop
                                Collections.sort(countryNameList);
                                setCountryAdapter();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());

                    }
                }

            }

            @Override
            public void onError(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());

            }
        });
    }

    private void setCountryAdapter() {
        flightHistoryView.setVisibility(View.VISIBLE);
        mainScrollView.smoothScrollTo(0,flightHistoryView.getTop());
        setCustomFonts();
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if(countryNameList!=null && countryNameList.size()>0){
            countryAdapter = new ArrayAdapter<>(activity,R.layout.country_spinner_item,countryNameList);
            spCountry.setAdapter(countryAdapter);
            if (isBackButtonClicked){
                spCountry.setText(countryString);
                Iterator myVeryOwnIterator = countryMap.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();
                    String value=(String)countryMap.get(key);
                    if (key.equalsIgnoreCase(countryString)){
                        tvCountryError.setVisibility(View.GONE);
                        countryIdString = value;
                        break;
                    }else if (countryString.length()>0 && !key.equalsIgnoreCase(countryString)){
                        tvCountryError.setVisibility(View.VISIBLE);
                        tvCountryError.setText("Country name does not exist.");
                    }else {
                        tvCountryError.setVisibility(View.GONE);
//                        tvCountryError.setText("Country name does not exist.");
                    }
                    // System.out.println("===================key "+value+"===="+key);
                }
            }
        }

    }

    private void setCustomFonts() {
        Typeface fontEng = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_regular.ttf");
        Typeface fontUrdu = Typeface.createFromAsset(getActivity().getAssets(),"notonastaliqurdu_regular.ttf");

        Typeface fontSindhi = Typeface.createFromAsset(getActivity().getAssets(),"sindhi_fonts.ttf");

        // for Country
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringCountry = new SpannableString(activity.getResources().getString(R.string.country_text_sindhi)+"*");
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontSindhi),13,spanStringCountry.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new ForegroundColorSpan(Color.RED),spanStringCountry.length()-1,spanStringCountry.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvCountry.setText(spanStringCountry);

        }else {
            SpannableString spanStringCountry = new SpannableString(tvCountry.getText().toString()+"*");
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontUrdu),13,spanStringCountry.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new ForegroundColorSpan(Color.RED),spanStringCountry.length()-1,spanStringCountry.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvCountry.setText(spanStringCountry);
        }

        // for Flight No
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringFlightNo = new SpannableString(activity.getResources().getString(R.string.flight_text_sindhi));
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontEng),0,10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontSindhi),11,spanStringFlightNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvFlightNo.setText(spanStringFlightNo);
        }else {
            SpannableString spanStringFlightNo = new SpannableString(tvFlightNo.getText().toString());
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontEng),0,10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontUrdu),11,spanStringFlightNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvFlightNo.setText(spanStringFlightNo);
        }

        // for Passport No
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringPassportNo = new SpannableString(activity.getResources().getString(R.string.passport_no_sindhi));
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontSindhi),13,spanStringPassportNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvPassportNo.setText(spanStringPassportNo);
        }else {
            SpannableString spanStringPassportNo = new SpannableString(tvPassportNo.getText().toString());
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontUrdu),13,spanStringPassportNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvPassportNo.setText(spanStringPassportNo);
        }

        // for Name
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringName = new SpannableString(activity.getResources().getString(R.string.name_text_sindhi));
            spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("",fontSindhi),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new ForegroundColorSpan(Color.RED),spanStringName.length()-1,spanStringName.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvName.setText(spanStringName);
        }else {
            SpannableString spanStringName = new SpannableString(tvName.getText().toString());
            spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("",fontUrdu),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new ForegroundColorSpan(Color.RED),spanStringName.length()-1,spanStringName.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvName.setText(spanStringName);
        }

        // for Mobile
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringMobile = new SpannableString(activity.getResources().getString(R.string.mobile_no1_sindhi));
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontSindhi),7,spanStringMobile.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobileNo.setText(spanStringMobile);
        }else {
            SpannableString spanStringMobile = new SpannableString(tvMobileNo.getText().toString());
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontUrdu),7,spanStringMobile.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMobileNo.setText(spanStringMobile);
        }



    }

    private void addAssessmentData() {
        progressDialog.setMessage("Add Assessment Data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new SelfAssessmentQuestionService(activity).addAssessmentData(questionsResultHashMap, countryIdString, passportNoString,
                flightNoString,optionListQuest8,otherDiseaseName, nameString,mobileNoString,new UserSession(activity).getOtherUserId(),
                new UserSession(activity).getOtherUserType(),new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if(response!=null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            try {
                                if(!response.isNull("statusDescription") && !response.getString("statusDescription").equalsIgnoreCase("")){
                                    String statusDescription = response.getString("statusDescription");
                                    String data = "";
                                    if(!response.isNull("data") && !response.getString("data").equalsIgnoreCase("")){
                                        data = response.getString("data");

                                    }
                                    if(statusDescription.equalsIgnoreCase("Success")){
                                        PublicSurveyResultFragment fragment =  new PublicSurveyResultFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("surveyResult","NegativeResult");
                                        bundle.putString("surveyMessage",data);
                                        fragment.setArguments(bundle);
                                        CommonCode.updateDisplay(fragment,getActivity().getSupportFragmentManager());
                                    }
                                    else{
                                        commonCode.showErrorORSuccessAlert(activity,"error","Sorry! your form could not be submitted at that moment. Please try again!",getActivity().getSupportFragmentManager(),null);
                                        /*PublicSurveyResultFragment fragment =  new PublicSurveyResultFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("surveyResult","NegativeResult");
                                        bundle.putString("surveyMessage",data);
                                        fragment.setArguments(bundle);
                                        CommonCode.updateDisplay(fragment,getActivity().getSupportFragmentManager());
*/
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());

                            }
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());

                    }
                });
    }

    private void addTextWatcher() {
        edPassportNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if(s.toString()==null || s.toString().equalsIgnoreCase("") || s.toString().isEmpty()){
                    tvPassportNoError.setVisibility(View.VISIBLE);
                }
                else {
                    tvPassportNoError.setVisibility(View.GONE);
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edFlightNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if(s.toString()==null || s.toString().equalsIgnoreCase("") || s.toString().isEmpty()){
                    tvFlightNoError.setVisibility(View.VISIBLE);
                }
                else {
                    tvFlightNoError.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()==null || s.toString().equalsIgnoreCase("") || s.toString().isEmpty()){
                    //tvNameError.setVisibility(View.VISIBLE);
                }
                else {
                    tvNameError.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mobileTextWatcher = new TextWatcher() {
            private int preLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edMobileNo.removeTextChangedListener(mobileTextWatcher);
                String str = edMobileNo.getText().toString();
                int currentLength = edMobileNo.getText().toString().length();
                int cursorPosition = edMobileNo.getSelectionStart();
                if(preLength < currentLength){
                    if(currentLength == 1){
                        if(str.equalsIgnoreCase("3")){
                            str = "+92"+str;
                            cursorPosition = cursorPosition+3;
                            tvMobNoError.setText("Mobile # is Invalid");
                            tvMobNoError.setVisibility(View.VISIBLE);
                        }else{
                            str = "";
                            cursorPosition = 0;
                            //tvMobNoError.setText("Mobile # is Required");
                            tvMobNoError.setVisibility(View.GONE);
                        }
                    }else if (currentLength >= 3 && currentLength <13){
                        tvMobNoError.setText("Mobile # is Invalid");
                        tvMobNoError.setVisibility(View.VISIBLE);
                    }else if (currentLength == 0){
                        //tvMobNoError.setText("Mobile # is Required");
                        tvMobNoError.setVisibility(View.GONE);
                    }else {
                        tvMobNoError.setVisibility(View.GONE);
                    }
                }else{
                    if(currentLength == 3){
                        str = "";
                        cursorPosition = 0;
                        //tvMobNoError.setText("Mobile # is Required");
                        tvMobNoError.setVisibility(View.GONE);
                    }else if (currentLength >= 3 && currentLength <13){
                        tvMobNoError.setText("Mobile # is Invalid");
                        tvMobNoError.setVisibility(View.VISIBLE);
                    }else if (currentLength == 0){
                       // tvMobNoError.setText("Mobile # is Required");
                        tvMobNoError.setVisibility(View.GONE);
                    }else {
                        tvMobNoError.setVisibility(View.GONE);
                    }
                }

                edMobileNo.setText(str);
                edMobileNo.setSelection(cursorPosition);
                preLength = edMobileNo.getText().toString().length();
                edMobileNo.addTextChangedListener(mobileTextWatcher);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        spCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spCountry.getText().toString().trim().length() != 0) {
                    tvCountryError.setVisibility(View.GONE);
                    countryString = spCountry.getText().toString();
                    /*countryAdapter = new ArrayAdapter<>(activity,R.layout.country_spinner_item,countryNameList);
                    spCountry.setAdapter(countryAdapter);
                    int countryPos = countryAdapter.getPosition(countryString);*/

                    Iterator myVeryOwnIterator = countryMap.keySet().iterator();
                    while(myVeryOwnIterator.hasNext()) {
                        String key=(String)myVeryOwnIterator.next();
                        String value=(String)countryMap.get(key);
                        if (key.equalsIgnoreCase(countryString)){
                            tvCountryError.setVisibility(View.GONE);
                            countryIdString = value;
                            break;
                        }else {
                            tvCountryError.setVisibility(View.VISIBLE);
                            tvCountryError.setText("Country name does not exist.");
                        }
                       // System.out.println("===================key "+value+"===="+key);
                    }
                }else {
                    tvCountryError.setVisibility(View.VISIBLE);
                    tvCountryError.setText("Please select Country.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void addAutoTextviewListener(){

        spCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideCustomKeyboard();
            }
        });

    }
    private Boolean personInfoValidate(){
        Boolean isNameValid = true,isMobNoValid = true;
        if(edName.getText().toString().trim()==null || edName.getText().toString().trim().isEmpty() ||
                edName.getText().toString().trim().equalsIgnoreCase("")){
            isNameValid = false;
            tvNameError.setVisibility(View.VISIBLE);
        }

        /*if(edMobileNo.getText().toString().trim()==null || edMobileNo.getText().toString().trim().isEmpty() ||
                edMobileNo.getText().toString().trim().equalsIgnoreCase("")){
            isMobNoValid = false;
            tvMobNoError.setText("Mobile # is Required");
            tvMobNoError.setVisibility(View.VISIBLE);
        }*/
        if(tvMobNoError.getVisibility()==View.VISIBLE){
            isMobNoValid = false;
        }

        return (isMobNoValid&&isNameValid);
    }

    @Override
    public void onResume() {
        super.onResume();
        edMobileNo.addTextChangedListener(mobileTextWatcher);
    }

    //For Custom Keyboard
    public void showCustomKeyboard( View v ) {
        keyboardview.setVisibility(View.VISIBLE);
        keyboardview.setEnabled(true);
        if( v!=null ) ((InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        if(NavigationDrawerActivity.isBackPressForKeyboard){
            hideCustomKeyboard();
            NavigationDrawerActivity.isBackPressForKeyboard  = false;
        }
        int height =0;
        for(Keyboard.Key key : keyboardQwertyFirst.getKeys()) {
            height += key.height;
        }
        if (edPassportNo.hasFocus()) {
            final int finalHeight = height;
            keyboardview.post(new Runnable() {
                @Override
                public void run() {
                    keyboardview.setVisibility(View.VISIBLE);
                    keyboardview.setEnabled(true);
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    if(selectedLanguage.equalsIgnoreCase("Urdu")){
                        layoutParams.height = keyboardview.getHeight() - (40+(btnNextFlight.getHeight()*3));
                    }else{
                        layoutParams.height = 100;

                    }
                    final int height =  btnNextFlight.getHeight()*2;
                    spacingViewFlighLayout.setLayoutParams(layoutParams);
                    spacingViewFlighLayout.setVisibility(View.VISIBLE);
                    spacingViewFlighLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(screenHeight>1280){
                                mainScrollView.smoothScrollTo(0,mainScrollView.getBottom());
                            }
                            else{
                                if(selectedLanguage.equalsIgnoreCase("Urdu")){
                                    mainScrollView.smoothScrollTo(0,screenHeight);
                                }
                                else{
                                    mainScrollView.smoothScrollTo(0,(screenHeight-height));
                                }

                            }
                        }
                    },250);
                }
            });
        }

        if (edFlightNo.hasFocus()) {
            final int finalHeight = height;
            keyboardview.post(new Runnable() {
                @Override
                public void run() {
                    keyboardview.setVisibility(View.VISIBLE);
                    keyboardview.setEnabled(true);
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);

                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    layoutParams.height = keyboardview.getHeight() - (40+(btnNextFlight.getHeight()*2));
                    spacingViewFlighLayout.setLayoutParams(layoutParams);
                    spacingViewFlighLayout.setVisibility(View.VISIBLE);
                    spacingViewFlighLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(screenHeight>1280){
                                mainScrollView.smoothScrollTo(0,mainScrollView.getBottom());
                            }
                            else{
                                if(selectedLanguage.equalsIgnoreCase("Urdu")){
                                    mainScrollView.smoothScrollTo(0,screenHeight-(btnNextFlight.getHeight()*2));
                                }
                                else{
                                    mainScrollView.smoothScrollTo(0,mainScrollView.getBottom());
                                }
                            }
                        }
                    },250);

                }
            });
        }

        if (spCountry.hasFocus()) {
            final int finalHeight = height;
            keyboardview.post(new Runnable() {
                @Override
                public void run() {
                    keyboardview.setVisibility(View.VISIBLE);
                    keyboardview.setEnabled(true);
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);

                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    if ((keyboardview.getHeight() - (10+(edPassportNo.getHeight()))) > 130){
                        layoutParams.height = 66;
                    }else {
                        layoutParams.height = keyboardview.getHeight() - (120 + (btnNextFlight.getHeight()*2));
                    }
                    spacingViewFlighLayout.setLayoutParams(layoutParams);
                    spacingViewFlighLayout.setVisibility(View.VISIBLE);
                    spacingViewFlighLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainScrollView.smoothScrollTo(0,(mainScrollView.getBottom()-50));
                        }
                    },250);
                    spCountry.showDropDown();
                }
            });
        }

        if (edName.hasFocus()) {
            keyboardview.post(new Runnable() {
                @Override
                public void run() {
                    keyboardview.setVisibility(View.VISIBLE);
                    keyboardview.setEnabled(true);
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) spacingViewPersonInfoLayout.getLayoutParams();
                    layoutParams.height = keyboardview.getHeight();
                    spacingViewPersonInfoLayout.setLayoutParams(layoutParams);
                    spacingViewPersonInfoLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(screenHeight>1280){
                                mainScrollView.smoothScrollTo(0,tvMobileNo.getBottom() +keyboardview.getHeight());
                            }
                            else{
                                if(selectedLanguage.equalsIgnoreCase("Urdu")){
                                    mainScrollView.smoothScrollTo(0,screenHeight-tvMobileNo.getBottom());
                                }
                                else{
                                    mainScrollView.smoothScrollTo(0,tvMobileNo.getBottom() +keyboardview.getHeight());
                                }
                            }

                        }
                    }, 250);
                }
            });
        }

        if (edOtherDisease.hasFocus()) {
            keyboardview.post(new Runnable() {
                @Override
                public void run() {
                    keyboardview.setVisibility(View.VISIBLE);
                    keyboardview.setEnabled(true);
                    viewKeyboard.getLayoutParams().height = keyboardview.getHeight();
                    viewKeyboard.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewEmptySubmit.getLayoutParams();
                    layoutParams.height = keyboardview.getHeight();
                    viewEmptySubmit.setLayoutParams(layoutParams);
                    viewEmptySubmit.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(screenHeight>1280){
                                mainScrollView.smoothScrollTo(0,mainScrollView.getBottom()-btnBack.getHeight() - btnSubmit.getHeight());
                            }
                            else{
                                if(selectedLanguage.equalsIgnoreCase("Urdu")){
                                    int height = screenHeight-btnBack.getHeight() - btnSubmit.getHeight();
                                    mainScrollView.smoothScrollTo(0,height);
                                }
                                else{
                                    int height = mainScrollView.getBottom()-(btnSubmit.getHeight()/2);
                                    mainScrollView.smoothScrollTo(0,height);
                                }
                            }
                        }
                    }, 250);
                }
            });

        }
    }
    public void hideCustomKeyboard() {
        keyboardview.setVisibility(View.GONE);
        keyboardview.setEnabled(false);
        viewKeyboard.setVisibility(View.GONE);
        viewKeyboardPerson.setVisibility(View.GONE);
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
                        if (id.equalsIgnoreCase("edPassportNo")){
                            edPassportNo.clearFocus();
                            hideCustomKeyboard();
                        }
                        else if(id.equalsIgnoreCase("edName")){
                            edName.clearFocus();
                            edMobileNo.requestFocus();
                            edMobileNo.setFocusable(true);
                            edMobileNo.setFocusableInTouchMode(true);
                            hideCustomKeyboard();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.showSoftInput(edMobileNo, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }else if(id.equalsIgnoreCase("edFlightNo")){
                            edFlightNo.clearFocus();
                            edPassportNo.requestFocus();
                            edPassportNo.setFocusable(true);
                            edPassportNo.setFocusableInTouchMode(true);
                            //hideCustomKeyboard();
                        }
                        else {
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

    private void addFocusChangeListener() {
        /*edFlightNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    edFlightNo.clearFocus();
                }
                else{
                    hideCustomKeyboard();
                    //showKeyboard(v);
                }
            }
        });*/

        edOtherDisease.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    commonEditText = edOtherDisease;
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

                }else{
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewEmptySubmit.getLayoutParams();
                    layoutParams.height = 100;
                    viewEmptySubmit.setLayoutParams(layoutParams);
                    hideCustomKeyboard();
                }
            }
        });

        edMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus || !edMobileNo.requestFocus()){
                    NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.VISIBLE);
                    edMobileNo.clearFocus();
                    commonCode.hideKeyboard(v);
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) spacingViewPersonInfoLayout.getLayoutParams();
                    layoutParams.height = 100;
                    spacingViewPersonInfoLayout.setLayoutParams(layoutParams);
                    mainScrollView.smoothScrollTo(0,mainScrollView.getBottom());
                }
                else{
                    hideCustomKeyboard();
                    NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.GONE);
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) spacingViewPersonInfoLayout.getLayoutParams();
                    layoutParams.height = 400;
                    spacingViewPersonInfoLayout.setLayoutParams(layoutParams);
                    spacingViewPersonInfoLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(screenHeight<=1280 && selectedLanguage.equalsIgnoreCase("Urdu")){
                                mainScrollView.smoothScrollTo(0,screenHeight);

                            }
                            else{
                                mainScrollView.smoothScrollTo(0,spacingViewPersonInfoLayout.getBottom());

                            }

                        }
                    }, 250);
//                    showKeyboard(v);


                    /*                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(edMobileNo, InputMethodManager.SHOW_IMPLICIT);*/

                }
            }
        });

        // For Passport No
        edPassportNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    commonEditText = edPassportNo;

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

                    EditText edittext = (EditText) v;
                    int inType = edittext.getInputType();       // Backup the input type
                    edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.setFocusableInTouchMode(hasFocus);               // Call native handler
                    edittext.setFocusable(hasFocus);
                    edittext.setInputType(inType);
                    showCustomKeyboard(v);// Restore input type

                }else if (!hasFocus){
                    edFlightNo.setFocusable(true);
                    edFlightNo.setFocusableInTouchMode(true);
                    hideCustomKeyboard();
                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    if(layoutParams.height > 100){
                        layoutParams.height = 100;
                        spacingViewFlighLayout.setLayoutParams(layoutParams);
                    }
                }

            }
        });

        // Passport Touch Listener

        edPassportNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonEditText = edPassportNo;
                if (edFlightNo.hasFocus()){
                    edFlightNo.clearFocus();
                    edPassportNo.requestFocus();
                    edPassportNo.setFocusable(true);
                    edPassportNo.setFocusableInTouchMode(true);
                }else if (spCountry.hasFocus()){
                    spCountry.dismissDropDown();
                    spCountry.clearFocus();
                    edPassportNo.requestFocus();
                    edPassportNo.setFocusable(true);
                    edPassportNo.setFocusableInTouchMode(true);
                }
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

        //for flight No
        edFlightNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    commonEditText = edFlightNo;

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

                    EditText edittext = (EditText) v;
                    int inType = edittext.getInputType();       // Backup the input type
                    edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.setFocusableInTouchMode(hasFocus);               // Call native handler
                    edittext.setFocusable(hasFocus);
                    edittext.setInputType(inType);
                    showCustomKeyboard(v);// Restore input type

                }else if (!hasFocus){
                    edPassportNo.setFocusable(true);
                    edPassportNo.setFocusableInTouchMode(true);
                    //hideCustomKeyboard();
                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    if(layoutParams.height > 100){
                        layoutParams.height = 100;
                        spacingViewFlighLayout.setLayoutParams(layoutParams);
                    }
                }

            }
        });

        // Flight No Touch Listener

        edFlightNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonEditText = edFlightNo;
                if (edPassportNo.hasFocus()){
                    edPassportNo.clearFocus();
                    edFlightNo.requestFocus();
                    edFlightNo.setFocusableInTouchMode(true);
                    edFlightNo.setFocusable(true);
                } else if (spCountry.hasFocus()){
                    spCountry.dismissDropDown();
                    spCountry.clearFocus();
                    edFlightNo.requestFocus();
                    edFlightNo.setFocusableInTouchMode(true);
                    edFlightNo.setFocusable(true);
                }
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


        //for Country
        spCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    commonAutoCompleteTv = spCountry;

                    keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_english);
                    keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_english_upper_casse);
                    keyboardNumbers = new Keyboard(activity,R.xml.numbers_english);
                    keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_english);
                    keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_english_next);
                    keyboardview.setKeyboard(keyboardQwertyFirst);
                    keyboardview.setPreviewEnabled(false);

                    hideCustomKeyboard();
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    commonAutoCompleteTv.setRawInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                    commonAutoCompleteTv.setTextIsSelectable(true);
                    keyListenersAutoCompleteTv();

                    AutoCompleteTextView edittext = (AutoCompleteTextView) v;
                    int inType = edittext.getInputType();       // Backup the input type
                    edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                    edittext.setFocusableInTouchMode(hasFocus);               // Call native handler
                    edittext.setFocusable(hasFocus);
                    edittext.setInputType(inType);
                    showCustomKeyboard(v);// Restore input type

                }else if (!hasFocus){
                    edFlightNo.setFocusable(true);
                    edFlightNo.setFocusableInTouchMode(true);
                    //hideCustomKeyboard();
                    ConstraintLayout.LayoutParams layoutParams =(ConstraintLayout.LayoutParams) spacingViewFlighLayout.getLayoutParams();
                    if(layoutParams.height > 100){
                        layoutParams.height = 100;
                        spacingViewFlighLayout.setLayoutParams(layoutParams);
                    }
                }

            }
        });

        // Country Touch Listener

        spCountry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonAutoCompleteTv = spCountry;
                if (edPassportNo.hasFocus()){
                    edPassportNo.clearFocus();
                    spCountry.requestFocus();
                    spCountry.setFocusableInTouchMode(true);
                    spCountry.setFocusable(true);
                } else if (edFlightNo.hasFocus()){
                    edFlightNo.clearFocus();
                    spCountry.requestFocus();
                    spCountry.setFocusableInTouchMode(true);
                    spCountry.setFocusable(true);
                }
                keyboardQwertyFirst =new Keyboard(activity,R.xml.qwerty_english);
                keyboardQwertySecond = new Keyboard(activity,R.xml.qwerty_english_upper_casse);
                keyboardNumbers = new Keyboard(activity,R.xml.numbers_english);
                keyboardPunctuationsFirst = new Keyboard(activity,R.xml.punctuations_english);
                keyboardPunctuationsSecond = new Keyboard(activity,R.xml.punctuations_english_next);
                keyboardview.setKeyboard(keyboardQwertyFirst);
                keyboardview.setPreviewEnabled(false);

                hideCustomKeyboard();
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                commonAutoCompleteTv.setRawInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                commonAutoCompleteTv.setTextIsSelectable(true);
                keyListenersAutoCompleteTv();
                AutoCompleteTextView edittext = (AutoCompleteTextView) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);
                showCustomKeyboard(v);// Restore input type
                edittext.setCursorVisible(true);

                return true;
            }
        });


        // For Name

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
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);
                showCustomKeyboard(v);// Restore input type
                edittext.setCursorVisible(true);
                return true;
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
                    hideCustomKeyboard();
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) spacingViewPersonInfoLayout.getLayoutParams();
                    layoutParams.height = 100;
                    spacingViewPersonInfoLayout.setLayoutParams(layoutParams);
                    spacingViewPersonInfoLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainScrollView.smoothScrollTo(0,mainScrollView.getBottom());
                        }
                    }, 250);
/*                    edName.clearFocus();
                    edMobileNo.setFocusable(true);
                    edMobileNo.setFocusableInTouchMode(true);
                    edMobileNo.requestFocus();*/
                    //showKeyboard(v);
                }
            }
        });

    }

    private EditText getEditText(){
        return commonEditText;
    }

    private void addBackPressAwareListener() {
        edPassportNo.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edPassportNo.clearFocus();
                }

                /*if(viewKeyboard!=null && viewKeyboard.getVisibility()==View.VISIBLE){
                    System.out.println("=====come inside");
                    UserInformationFragment.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edPassportNo.clearFocus();
                }*/
            }
        });

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
        edOtherDisease.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edOtherDisease.clearFocus();
                }
            }
        });

        edFlightNo.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
               /* NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.VISIBLE);
                edFlightNo.clearFocus();*/
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    edFlightNo.clearFocus();
                }
            }
        });

        spCountry.setOnBackPressedListener(new BackPressAwareAutoTextview.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareAutoTextview awareEdittext) {
                if(keyboardview!=null && keyboardview.getVisibility()==View.VISIBLE){
                    NavigationDrawerActivity.isBackPressForKeyboard = true;
                    hideCustomKeyboard();
                    spCountry.clearFocus();
                }
            }
        });


        edMobileNo.setOnBackPressedListener(new BackPressAwareEdittext.BackPressedListener() {
            @Override
            public void onImeBack(BackPressAwareEdittext awareEdittext) {
                NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.VISIBLE);
                edMobileNo.clearFocus();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void scrollViewListener() {

        mainScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                commonCode.hideKeyboard(v);
                hideCustomKeyboard();

                if (edFlightNo.hasFocus() || edPassportNo.hasFocus() || edName.hasFocus()
                        || edMobileNo.hasFocus() || edOtherDisease.hasFocus() || spCountry.hasFocus() ) {
                    edMobileNo.clearFocus();
                    edPassportNo.clearFocus();
                    edName.clearFocus();
                    edFlightNo.clearFocus();
                    edOtherDisease.clearFocus();
                    spCountry.clearFocus();
                }
                return false;
            }
        });
    }

    private void showKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.GONE);

            inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            showHideKeyboardStatus();
        }
    }



    private void showHideKeyboardStatus(){

        final ViewTreeObserver viewTreeObserver = mainScrollView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int mPreviousHeight;

            @Override
            public void onGlobalLayout() {
                int newHeight = mainScrollView.getHeight();
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight > newHeight) {
                        // Height decreased: keyboard was shown
                        NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.GONE);

                    } else if (mPreviousHeight < newHeight) {
                        // Height increased: keyboard was hidden
                        NavigationDrawerActivity.layoutBottomButtons.setVisibility(View.VISIBLE);
                        if(edMobileNo.hasFocus()){
                            edMobileNo.clearFocus();
                        }
                        if (mainScrollView.getViewTreeObserver().isAlive()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mainScrollView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                mainScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        }

                    }
                    else {
                        // No change
                    }
                }

                mPreviousHeight = newHeight;
            }
        });
    }

    private void setQuestionNum(int index, int total){
        tvQuestionNo.setVisibility(View.VISIBLE);
        if (selectedLanguage.equalsIgnoreCase("en")) {
            tvQuestionNo.setText("Question " + index + " of " + total);
        }else if (selectedLanguage.equalsIgnoreCase("Urdu")){
            tvQuestionNo.setText("سوال "+total+" میں سے "+index);
        }else if (selectedLanguage.equalsIgnoreCase("sindhi")){
            tvQuestionNo.setText("سوال "+total +" مان "+index);
        }
    }
    private void disableCheckBoxex(LinearLayout linearLayoutAnswers,String checkString){
        for (int j=0;j<linearLayoutAnswers.getChildCount();j++) {
            View nextChild = linearLayoutAnswers.getChildAt(j);
            if (nextChild instanceof CheckBox) {
                CheckBox check = (CheckBox) nextChild;
                if (!check.getText().toString().trim().equalsIgnoreCase(checkString)) {
                    if (check.isChecked()) {
                        check.setChecked(false);
                    }
                    check.setEnabled(false);
                }
            }
        }
    }

    public void keyListenersAutoCompleteTv(){
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

               /* EditorInfo in = new EditorInfo();
                in.inputType = EditorInfo.TYPE_CLASS_TEXT;
                InputMethodService inputMethodService = new InputMethodService();*/
                InputConnection ic = commonAutoCompleteTv.onCreateInputConnection(new EditorInfo());
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

                        String ids[] = commonAutoCompleteTv.getResources().getResourceName(commonAutoCompleteTv.getId()).split("/");
                        String id="";
                        if (ids.length>1){
                            id = ids[1];
                        }
                       if(id.equalsIgnoreCase("spin_dist_user_info_frag")){
                            spCountry.clearFocus();
                            edFlightNo.requestFocus();
                            edFlightNo.setFocusable(true);
                            edFlightNo.setFocusableInTouchMode(true);
                            //hideCustomKeyboard();
                        }
                        else {
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


    @Override
    public void onPause() {
        super.onPause();
        if(keyboardview.getVisibility() == View.VISIBLE || viewKeyboard.getVisibility() == View.VISIBLE){
            hideCustomKeyboard();
        }
    }

    private int getHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }
}
