package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

public class NovelCoronaVirusDetails extends Fragment {


    private Activity activity;
    private View view;
    private TextView coronaVEngTV,coronaVUrduTV,coronaDesEngTV,coronaDesUrduTV,covidEngTV,
            covidUrduTV,covidDesEngTV,covidDesUrduTV;
    private LinearLayout urduContainerLL,engContainerLL;
    private UserSession session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel_corona_virus_details, container, false);
        this.activity = (Activity)getContext();
        NavigationDrawerActivity.tvHeading.setText("AWARENESS");
        session = new UserSession(activity);
        init();

//        Removed by Kamran on 10 April 2020
        /*SpannableString spanString = new SpannableString(detailsTV.getText().toString());
        spanString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.app_color_green)),
                35,59,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.app_color_green)),
                detailsTV.length() - 22,detailsTV.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        detailsTV.setText(spanString);*/

        String selectedLanguage = session.getSelectedLanguage();
        System.out.println("---@@## selectedLanguage : "+selectedLanguage);
        if(selectedLanguage.equalsIgnoreCase("en")){
            urduContainerLL.setVisibility(View.GONE);
            engContainerLL.setVisibility(View.VISIBLE);

            /*coronaVUrduTV.setVisibility(View.GONE);
            covidUrduTV.setVisibility(View.GONE);
            coronaDesUrduTV.setVisibility(View.GONE);
            covidDesUrduTV.setVisibility(View.GONE);

            coronaVEngTV.setVisibility(View.VISIBLE);
            covidEngTV.setVisibility(View.VISIBLE);
            coronaDesEngTV.setVisibility(View.VISIBLE);
            covidDesEngTV.setVisibility(View.VISIBLE);*/

            coronaVEngTV.setText(R.string.what_is_coronavirus_eng);
            coronaDesEngTV.setText(R.string.coronavirus_des_eng);
            covidEngTV.setText(R.string.covid_19_eng);
            covidDesEngTV.setText(R.string.covid_19_des_eng);
        }else if(selectedLanguage.equalsIgnoreCase("Urdu")){
            /*coronaVEngTV.setVisibility(View.GONE);
            covidEngTV.setVisibility(View.GONE);
            coronaDesEngTV.setVisibility(View.GONE);
            covidDesEngTV.setVisibility(View.GONE);

            coronaVUrduTV.setVisibility(View.VISIBLE);
            covidUrduTV.setVisibility(View.VISIBLE);
            coronaDesUrduTV.setVisibility(View.VISIBLE);
            covidDesUrduTV.setVisibility(View.VISIBLE);*/

            urduContainerLL.setVisibility(View.VISIBLE);
            engContainerLL.setVisibility(View.GONE);

            coronaVUrduTV.setText("   "+activity.getResources().getString(R.string.what_is_coronavirus_urdu));
            coronaDesUrduTV.setText("   "+activity.getResources().getString(R.string.coronavirus_des_urdu));
            covidUrduTV.setText("   "+activity.getResources().getString(R.string.covid_19_urdu));
            covidDesUrduTV.setText("   "+activity.getResources().getString(R.string.covid_19_des_urdu));

            coronaVUrduTV.setIncludeFontPadding(false);
            coronaDesUrduTV.setIncludeFontPadding(false);
            covidUrduTV.setIncludeFontPadding(false);
            covidDesUrduTV.setIncludeFontPadding(false);

            Typeface fontUrdu = Typeface.createFromAsset(activity.getAssets(),"notonastaliqurdu_regular.ttf");
            coronaVUrduTV.setTypeface(fontUrdu);
            coronaDesUrduTV.setTypeface(fontUrdu);
            covidDesUrduTV.setTypeface(fontUrdu);
            covidUrduTV.setTypeface(fontUrdu);

        }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
            /*coronaVEngTV.setVisibility(View.GONE);
            covidEngTV.setVisibility(View.GONE);
            coronaDesEngTV.setVisibility(View.GONE);
            covidDesEngTV.setVisibility(View.GONE);

            coronaVUrduTV.setVisibility(View.VISIBLE);
            covidUrduTV.setVisibility(View.VISIBLE);
            coronaDesUrduTV.setVisibility(View.VISIBLE);
            covidDesUrduTV.setVisibility(View.VISIBLE);*/

            urduContainerLL.setVisibility(View.VISIBLE);
            engContainerLL.setVisibility(View.GONE);

            coronaVUrduTV.setText(R.string.what_is_coronavirus_sindhi);
            coronaDesUrduTV.setText(R.string.coronavirus_des_sindhi);
            covidUrduTV.setText(R.string.covid_19_sindhi);
            covidDesUrduTV.setText(R.string.covid_19_des_sindhi);

            Typeface fontSindhi = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            coronaVUrduTV.setTypeface(fontSindhi,Typeface.BOLD);
            /*coronaDesUrduTV.setTypeface(fontSindhi,Typeface.BOLD);*/
            /*covidDesUrduTV.setTypeface(fontSindhi,Typeface.BOLD);*/
            covidUrduTV.setTypeface(fontSindhi,Typeface.BOLD);
        }
        return view;
    }

    private void init(){
//        detailsTV = view.findViewById(R.id.tv_details_novel_corona_virus_frag);
        coronaVEngTV = view.findViewById(R.id.tv_what_is_coronavirus_eng_novel_corona_virus_details_frag);
        coronaDesEngTV = view.findViewById(R.id.tv_des_coronaviruses_novel_corona_virus_details_frag);
        covidEngTV = view.findViewById(R.id.tv_what_is_covid_19_eng_novel_corona_virus_details_frag);
        covidDesEngTV  = view.findViewById(R.id.tv_des_covid_novel_corona_virus_details_frag);
        coronaVUrduTV = view.findViewById(R.id.tv_what_is_coronavirus_urdu_novel_corona_virus_details_frag);
        coronaDesUrduTV = view.findViewById(R.id.tv_des_coronaviruses_urdu_novel_corona_virus_details_frag);
        covidUrduTV = view.findViewById(R.id.tv_what_is_covid_19_urdu_novel_corona_virus_details_frag);
        covidDesUrduTV = view.findViewById(R.id.tv_des_covid_urdu_novel_corona_virus_details_frag);
        urduContainerLL = view.findViewById(R.id.ll_urdu_tv_container_novel_corona_details_frag);
        engContainerLL = view.findViewById(R.id.ll_eng_tv_container_novel_corona_details_frag);
    }
}
