package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.IsolationItemRVAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.QuarantineRVAdapter;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuarantineFragment extends Fragment {

    private View view;
    private Activity activity;
    private RecyclerView rvItems;
    private TextView tvQuarantineHeading, tvSymptoms;
    private List<String> itemsBeanList;
    private String selectedLanguage="";
    private QuarantineRVAdapter quarantineRVAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quarantine, container, false);
        activity = (Activity) getContext();
        init();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        itemsBeanList = new ArrayList<>();

        if (selectedLanguage.equalsIgnoreCase("en")){
            Typeface fontEng = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.quarantine_heading_text));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvQuarantineHeading.setText(spannableStringIsolation);
            tvQuarantineHeading.setTypeface(fontEng);

            SpannableString spannableStringMonitor = new SpannableString(activity.getResources().getString(R.string.monitorSymptomsText));
            spannableStringMonitor.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringMonitor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSymptoms.setText(spannableStringMonitor);
            tvSymptoms.setTypeface(fontEng);

            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.quarantineList));

        }else if (selectedLanguage.equalsIgnoreCase("urdu")){
            Typeface fontUrdu = Typeface.createFromAsset(activity.getAssets(),"notonastaliqurdu_regular.ttf");
            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.quarantine_heading_text_urdu));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvQuarantineHeading.setText(spannableStringIsolation);
            tvQuarantineHeading.setTypeface(fontUrdu);
            tvQuarantineHeading.setLineSpacing(0,0.90f);

            SpannableString spannableStringMonitor = new SpannableString(activity.getResources().getString(R.string.monitorSymptomsTextUrdu));
            spannableStringMonitor.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringMonitor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSymptoms.setText(spannableStringMonitor);
            tvSymptoms.setTypeface(fontUrdu);
            tvSymptoms.setLineSpacing(0,0.90f);

            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.quarantineListUrdu));

        }else if (selectedLanguage.equalsIgnoreCase("sindhi")){
            Typeface fontSindhi = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            SpannableString spannableStringIsolation = new SpannableString(activity.getResources().getString(R.string.quarantine_heading_text_sindhi));
            spannableStringIsolation.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringIsolation.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvQuarantineHeading.setText(spannableStringIsolation);
            tvQuarantineHeading.setTypeface(fontSindhi);
            tvQuarantineHeading.setLineSpacing(0,0.85f);
            tvQuarantineHeading.setPadding(10,6,10,10);

            SpannableString spannableStringMonitor = new SpannableString(activity.getResources().getString(R.string.monitorSymptomsTextSindhi));
            spannableStringMonitor.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringMonitor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSymptoms.setText(spannableStringMonitor);
            tvSymptoms.setTypeface(fontSindhi);

            itemsBeanList = Arrays.asList(activity.getResources().getStringArray(R.array.quarantineListSindhi));

        }
        rvItems.setHasFixedSize(true);
        rvItems.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity).build());
        rvItems.setLayoutManager(new LinearLayoutManager(activity));
        quarantineRVAdapter = new QuarantineRVAdapter(activity,itemsBeanList);
        rvItems.setAdapter(quarantineRVAdapter);
        quarantineRVAdapter.notifyDataSetChanged();
        if(Build.VERSION.SDK_INT<=22){
            rvItems.setNestedScrollingEnabled(false);
        }
        return view;
    }

    private void init(){
        rvItems = view.findViewById(R.id.rvQuarantineItemsQuarantineFrag);
        tvQuarantineHeading = view.findViewById(R.id.tvQuarantineHeadingQuarantineFrag);
        tvSymptoms = view.findViewById(R.id.tvMonitorSymptomsQuarantineFrag);
    }
}
