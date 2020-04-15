package com.sapphire.HealthAssessmentPDMA.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.bean.OtherUserListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PublicSurveyResultFragment extends Fragment {

    private View view;
    private Activity activity;
    private TextView tvActionNeeded,tvResultText,textViewFirst;
    private String surveyResult="",surveyMsg = "";
    private Button otherAssessmentBTN, trackAssessmentBTN;
    private CommonCode commonCode;

    public static Boolean isComeFromOtherAssess = false;

    public PublicSurveyResultFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        NavigationDrawerActivity.tvHeading.setText("");
        commonCode = new CommonCode(activity);
        if(getArguments()!=null){
           surveyResult  = getArguments().getString("surveyResult");
           surveyMsg = getArguments().getString("surveyMessage");
           isComeFromOtherAssess = getArguments().getBoolean("isOtherAssessment");
           if(surveyResult!=null){
               if(surveyResult.equalsIgnoreCase("NegativeResult")){
                   view = inflater.inflate(R.layout.fragment_public_survey_result_negative, container, false);
               }
               else{
                   view = inflater.inflate(R.layout.fragment_public_survey_result_positive,container,false);
               }
           }

       }
        init();

        if (surveyMsg.length()>0){
            String messageArr[] = surveyMsg.split(":");
            if (messageArr.length>1){
                SpannableString spannableString = new SpannableString(messageArr[0]);
                /*spannableString.setSpan(new AbsoluteSizeSpan(20),0,9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tvResultText.setText(spannableString);*/

                spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,9,0);
                spannableString.setSpan(new RelativeSizeSpan(1.3f),0,9,0);
                textViewFirst.setText(spannableString, TextView.BufferType.SPANNABLE);
                tvResultText.setText(messageArr[1].trim());
            }
        }
        //tvResultText.setText(surveyMsg);
        tvActionNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // CommonCode.updateDisplay(new PublicListingFragment(),getActivity().getSupportFragmentManager());
            }
        });
        if(otherAssessmentBTN != null){
            otherAssessmentBTN.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //CommonCode.updateDisplay(new OtherUserInformationFragment(),getFragmentManager());
                    getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
                }
            });
        }

        if(trackAssessmentBTN != null){
            trackAssessmentBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                    CommonCode.updateDisplay(new PublicListingFragment(),getFragmentManager());
                }
            });
        }
        return view;
    }

    private void init() {
        if(surveyResult!=null && surveyResult.equalsIgnoreCase("NegativeResult")){
            tvActionNeeded = view.findViewById(R.id.tv_no_actionNeed_PSRNegFrag);
            tvResultText = view.findViewById(R.id.tv_resMsg_PSRNegFrag);
            textViewFirst = view.findViewById(R.id.tv_resTitle_PSRNegFrag);
            trackAssessmentBTN = view.findViewById(R.id.btn_track_assessment_public_survey_result_neg);
            otherAssessmentBTN = view.findViewById(R.id.btn_other_assessment_public_survey_result_neg);
        }
        else{
            tvActionNeeded = view.findViewById(R.id.tv_actionNeed_PSRNegFrag);
            tvResultText = view.findViewById(R.id.tv_resMsg_PSRPosFrag);
        }

    }

    private void getUserHavingOtherAssessments(final Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final List<OtherUserListingRVAdapterBean> beanList = new ArrayList<>();

        new UserService(activity).getUsersHavingAssessments(userId,"other",new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                String publicUserId="",userType="",userMobileNumber="", userName="";
                if(response != null && response.length() > 0 && !response.isNull("statusDescription")){
                    try {
                        String status = response.getString("statusDescription");
                        if(status != null && status.length() > 0 && status.equalsIgnoreCase("Success") ){
                            JSONObject data = response.getJSONObject("data");
                            int count=0;
                            if(data != null && data.length() > 0){
                                for (int i=1; i<=data.length();i++){
                                    count++;
                                    if (!data.isNull(""+count)){
                                        JSONObject innerObj = data.getJSONObject(""+count);
                                        //for (int j=0; j<innerObj.length();j++){
                                        if (!innerObj.isNull("public_user_id")) {
                                            publicUserId = innerObj.getString("public_user_id");
                                        }
                                        if (!innerObj.isNull("name")) {
                                            userName = innerObj.getString("name");
                                        }
                                        if (!innerObj.isNull("user_type")) {
                                            userType = innerObj.getString("user_type");
                                        }
                                        if (!innerObj.isNull("mobile")){
                                            userMobileNumber = innerObj.getString("mobile");
                                        }

                                        beanList.add(new OtherUserListingRVAdapterBean(publicUserId,userName,userMobileNumber,userType));
                                    }else {
                                        i--;
                                    }

                                }
                                if (beanList.size()>0){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dataList",new Gson().toJson(beanList));
                                    Fragment fragmentOtherUserListing = new OtherUserListingFragment();
                                    fragmentOtherUserListing.setArguments(bundle);
                                    getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                                    CommonCode.updateDisplay(fragmentOtherUserListing,getFragmentManager());
                                }else {
                                    getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                                    CommonCode.updateDisplay(new OtherUserInformationFragment(), getFragmentManager());
                                }
                            }else{
                                progressDialog.dismiss();
                                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again1!",getFragmentManager(),null,false);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        System.out.println("===================ex"+e);
                        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Value [] at data of type org.json.JSONArray cannot be converted to JSONObject")){
                            getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getFragmentManager());
                        }else{
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again2!",getFragmentManager(),null,false);
                        }

                    }

                }else{
                    progressDialog.dismiss();
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getFragmentManager(),null,false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getFragmentManager(),null,false);
            }
        });
    }

}
