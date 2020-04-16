package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.IsolationItemRVAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.PreventionRVAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.SeekMedicalAttentionRvAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.SeekMedicalAttentionRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.icu.lang.UProperty.INT_START;

public class MonitorFragment extends Fragment {

    private View view;
    private Activity activity;
    private RecyclerView rvItems, rvIcons;
    private TextView tvIsolationHeading, tvMedicalHeading;
    private List<String> itemsBeanList, seekMedicalAttentionStringList;
    private String selectedLanguage="";
    private IsolationItemRVAdapter isolationItemRVAdapter;
    private List<SeekMedicalAttentionRVAdapterBean> seekMedicalAttentionRVAdapterBeanList;
    private SeekMedicalAttentionRvAdapter seekMedicalAttentionRvAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_monitor, container, false);
        activity = (Activity) getContext();
        itemsBeanList = new ArrayList<>();
        seekMedicalAttentionStringList = new ArrayList<>();
        init();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        seekMedicalAttentionRVAdapterBeanList = new ArrayList<>();
        seekMedicalAttentionRVAdapterBeanList.add(new SeekMedicalAttentionRVAdapterBean(R.mipmap.difficulty_breathing));
        seekMedicalAttentionRVAdapterBeanList.add(new SeekMedicalAttentionRVAdapterBean(R.mipmap.chest_pain));
        seekMedicalAttentionRVAdapterBeanList.add(new SeekMedicalAttentionRVAdapterBean(R.mipmap.blue_face_lips));

        if (selectedLanguage.equalsIgnoreCase("en")) {
            Typeface fontEng = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.isolation_heading_text));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvIsolationHeading.setText(spannableStringIsolation);
            tvIsolationHeading.setTypeface(fontEng);

            SpannableString spannableMedical = new SpannableString(activity.getResources().getString(R.string.seek_medical_attention_heading_text));
            spannableMedical.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableMedical.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMedicalHeading.setText(spannableMedical);
            tvMedicalHeading.setTypeface(fontEng);

            seekMedicalAttentionStringList = Arrays.asList(activity.getResources().getStringArray(R.array.seekMedicalAttentionMonitor));
            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.howToCopeUpWithIsolationStressList));
        }else if (selectedLanguage.equalsIgnoreCase("urdu")) {
            Typeface fontUrdu = Typeface.createFromAsset(activity.getAssets(),"notonastaliqurdu_regular.ttf");

            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.isolation_heading_text_urdu));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvIsolationHeading.setText(spannableStringIsolation);
            tvIsolationHeading.setTypeface(fontUrdu);
            tvIsolationHeading.setLineSpacing(0,0.90f);

            SpannableString spannableMedical = new SpannableString(activity.getResources().getString(R.string.seek_medical_attention_heading_text_urdu));
            spannableMedical.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableMedical.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMedicalHeading.setText(spannableMedical);
            tvMedicalHeading.setTypeface(fontUrdu);
            tvMedicalHeading.setLineSpacing(0,0.90f);
            seekMedicalAttentionStringList = Arrays.asList(activity.getResources().getStringArray(R.array.seekMedicalAttentionMonitorUrdu));
            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.howToCopeUpWithIsolationStressListUrdu));
        }else if (selectedLanguage.equalsIgnoreCase("sindhi")) {
            Typeface fontSindhi = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");

            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.isolation_heading_text_sindhi));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvIsolationHeading.setText(spannableStringIsolation);
            tvIsolationHeading.setTypeface(fontSindhi);
            tvIsolationHeading.setLineSpacing(0,0.85f);
            tvIsolationHeading.setPadding(10,6,10,10);

            SpannableString spannableMedical = new SpannableString(activity.getResources().getString(R.string.seek_medical_attention_heading_text_sindhi));
            spannableMedical.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableMedical.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMedicalHeading.setText(spannableMedical);
            tvMedicalHeading.setTypeface(fontSindhi);
            tvMedicalHeading.setLineSpacing(0,0.85f);
            tvMedicalHeading.setPadding(76,6,70,12);
            seekMedicalAttentionStringList = Arrays.asList(activity.getResources().getStringArray(R.array.seekMedicalAttentionMonitorSindhi));
            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.howToCopeUpWithIsolationStressListSindhi));
        }

        rvIcons.setHasFixedSize(true);
        RecyclerView.LayoutManager  gridLayoutManager = new GridLayoutManager(activity,3)
        {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvIcons.setLayoutManager(gridLayoutManager);
        seekMedicalAttentionRvAdapter = new SeekMedicalAttentionRvAdapter(activity,seekMedicalAttentionRVAdapterBeanList,seekMedicalAttentionStringList);
        rvIcons.setAdapter(seekMedicalAttentionRvAdapter);


        rvItems.setHasFixedSize(true);

        rvItems.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity).build());
        rvItems.setLayoutManager(new LinearLayoutManager(activity));
        isolationItemRVAdapter = new IsolationItemRVAdapter(activity,itemsBeanList);
        rvItems.setAdapter(isolationItemRVAdapter);
        isolationItemRVAdapter.notifyDataSetChanged();
        if(Build.VERSION.SDK_INT<=22){
            rvItems.setNestedScrollingEnabled(false);
        }
        return view;
    }

    private void init(){
        rvIcons = view.findViewById(R.id.rvMonitorHeaderMonitorFrag);
        rvItems = view.findViewById(R.id.rvIsolationItemsMonitorFrag);
        tvIsolationHeading = view.findViewById(R.id.tvIsolationStressHeadingMonitorFrg);
        tvMedicalHeading = view.findViewById(R.id.tvSeekMedicalAttentionHeading);
    }
}
