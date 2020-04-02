package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;

public class NovelCoronaVirusDetails extends Fragment {


    private Activity activity;
    private View view;
    private TextView detailsTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_novel_corona_virus_details, container, false);
        this.activity = (Activity)getContext();
        NavigationDrawerActivity.tvHeading.setText("AWARENESS");
        init();

        SpannableString spanString = new SpannableString(detailsTV.getText().toString());
        spanString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.app_color_green)),
                35,59,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.app_color_green)),
                detailsTV.length() - 22,detailsTV.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        detailsTV.setText(spanString);

        return view;
    }

    private void init(){
        detailsTV = view.findViewById(R.id.tv_details_novel_corona_virus_frag);
    }
}
