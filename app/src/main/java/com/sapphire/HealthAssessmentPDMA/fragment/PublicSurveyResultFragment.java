package com.sapphire.HealthAssessmentPDMA.fragment;


import android.app.Activity;
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

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;


public class PublicSurveyResultFragment extends Fragment {

    private View view;
    private Activity activity;
    private TextView tvActionNeeded,tvResultText,textViewFirst;
    private String surveyResult="",surveyMsg = "";
    private Button otherAssessmentBTN, trackAssessmentBTN;
    private CommonCode commonCode;
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
                    getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                    CommonCode.updateDisplay(new OtherUserInformationFragment(),getFragmentManager());
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


}
