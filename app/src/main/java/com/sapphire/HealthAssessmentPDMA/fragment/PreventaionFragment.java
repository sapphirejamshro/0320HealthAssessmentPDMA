package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.PreventionRVAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.PreventionRVHeaderAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.PreventionRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PreventaionFragment extends Fragment {


    private Activity activity;
    private View view;
    private RecyclerView preventionRV, rvHeader;
    TextView tvTitle;
    private PreventionRVHeaderAdapter preventionRVHeaderAdapter;
    private PreventionRVAdapter preventionRVAdapter;
    private List<PreventionRVAdapterBean> beanListHeader;
    private List<String> beanList;
    private String selectedLanguage="";
    private boolean isEnglish =false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preventions,container,false);
        activity = (Activity)getContext();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        init();

        preventionRV.setHasFixedSize(true);
        rvHeader.setHasFixedSize(true);
        RecyclerView.LayoutManager  gridLayoutManager = new GridLayoutManager(activity,4)
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
        rvHeader.setLayoutManager(gridLayoutManager);

        beanList = new ArrayList<>();
        if (selectedLanguage.equalsIgnoreCase("en")) {
            isEnglish = true;
            Typeface fontEng = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            SpannableString spannableTitle = new SpannableString(activity.getResources().getString(R.string.prevention_text));
            spannableTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(spannableTitle);
            tvTitle.setTypeface(fontEng);

            beanList = Arrays.asList(activity.getResources().getStringArray(R.array.preventionList));
        }else if (selectedLanguage.equalsIgnoreCase("urdu")) {
            Typeface fontUrdu = Typeface.createFromAsset(activity.getAssets(),"notonastaliqurdu_regular.ttf");
            SpannableString spannableTitle = new SpannableString(activity.getResources().getString(R.string.prevention_text_urdu));
            spannableTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(spannableTitle);
            tvTitle.setTypeface(fontUrdu);
            tvTitle.setTextSize(30);
            tvTitle.setLineSpacing(0,0.65f);
            tvTitle.setPadding(15,-18,15,-18);
            isEnglish=false;
            beanList = Arrays.asList(activity.getResources().getStringArray(R.array.preventionListUrdu));
        }else if (selectedLanguage.equalsIgnoreCase("sindhi")) {
            Typeface fontSindhi = Typeface.createFromAsset(activity.getAssets(),"myriad_pro_regular.ttf");
            SpannableString spannableTitle = new SpannableString(activity.getResources().getString(R.string.prevention_text_sindhi));
            spannableTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(spannableTitle);
            tvTitle.setTypeface(fontSindhi);

            tvTitle.setTextSize(30);
            tvTitle.setLineSpacing(0,0.85f);
            tvTitle.setPadding(10,10,10,10);
            isEnglish = false;
            beanList = Arrays.asList(activity.getResources().getStringArray(R.array.preventionListSindhi));
        }
        beanListHeader = new ArrayList<>();

        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_1));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_2));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_3));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_4));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_5));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_6));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_7));
        beanListHeader.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_8));


        preventionRVHeaderAdapter = new PreventionRVHeaderAdapter(activity,beanListHeader);
        rvHeader.setAdapter(preventionRVHeaderAdapter);


        preventionRV.setLayoutManager(new LinearLayoutManager(activity));
        preventionRVAdapter = new PreventionRVAdapter(activity,beanList,isEnglish);
        preventionRV.setAdapter(preventionRVAdapter);
        preventionRVAdapter.notifyDataSetChanged();
        if(Build.VERSION.SDK_INT<=22){
            preventionRV.setNestedScrollingEnabled(false);
        }
        return view;
    }


    private void init(){
        preventionRV = view.findViewById(R.id.rv_prevention_frag);
        rvHeader = view.findViewById(R.id.rv_prevention_header_frag);
        tvTitle = view.findViewById(R.id.tv_title_prevention_frag);
    }
}
