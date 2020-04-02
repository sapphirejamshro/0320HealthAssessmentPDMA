package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.SymptomsAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.SymptomsBean;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.ArrayList;
import java.util.List;

public class SymptomsFragment extends Fragment {

    private ExpandableHeightGridView gridView;
    private TextView tvPrecautionPoint1, tvPrecautionPoint2,tvPrecautionPoint3, tvPrecautionPoint4;
    private View view1, view2, view3, view4;
    public static View view;
    private Activity activity;
    public static List<SymptomsBean> symptomsBeanList;
    private SymptomsAdapter symptomsAdapter;
    private int getWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_symptoms, container, false);
        activity = (Activity) getContext();
        init();
        symptomsBeanList = new ArrayList<>();
        String selectedLanguage = new UserSession(activity).getSelectedLanguage();
        if(selectedLanguage.equalsIgnoreCase("Urdu")){
            symptomsBeanList.add(new SymptomsBean(R.string.body_aches_urdu,R.mipmap.body_ache_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.difficulty_in_breathing_urdu,R.mipmap.difficulty_in_breathing_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.cough_urdu,R.mipmap.cough_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.fever_urdu,R.mipmap.fever_icon));
        }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
            symptomsBeanList.add(new SymptomsBean(R.string.body_aches_sindhi,R.mipmap.body_ache_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.difficulty_in_breathing_sindhi,R.mipmap.difficulty_in_breathing_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.cough_sindhi,R.mipmap.cough_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.fever_sindhi,R.mipmap.fever_icon));
        }else if(selectedLanguage.equalsIgnoreCase("en")){
            symptomsBeanList.add(new SymptomsBean(R.string.body_aches_eng,R.mipmap.body_ache_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.difficulty_in_breathing_eng,R.mipmap.difficulty_in_breathing_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.cough_eng,R.mipmap.cough_icon));
            symptomsBeanList.add(new SymptomsBean(R.string.fever_eng,R.mipmap.fever_icon));
        }

        symptomsAdapter = new SymptomsAdapter(activity,symptomsBeanList,true);
        gridView.setAdapter(symptomsAdapter);
        gridView.setExpanded(true);

        tvPrecautionPoint1.post(new Runnable() {
            @Override
            public void run() {
                getWidth =  tvPrecautionPoint1.getWidth();
            }
        });
        view1.getLayoutParams().width = getTextViewHeight(tvPrecautionPoint1.getWidth());
        tvPrecautionPoint2.post(new Runnable() {
            @Override
            public void run() {
                getWidth =  tvPrecautionPoint2.getWidth();
            }
        });
        view2.getLayoutParams().width = getTextViewHeight(tvPrecautionPoint2.getWidth());
        tvPrecautionPoint3.post(new Runnable() {
            @Override
            public void run() {
                getWidth =  tvPrecautionPoint3.getWidth();
            }
        });
        view3.getLayoutParams().width = getTextViewHeight(tvPrecautionPoint3.getWidth());
        tvPrecautionPoint4.post(new Runnable() {
            @Override
            public void run() {
                getWidth =  tvPrecautionPoint4.getWidth();
            }
        });
        view4.getLayoutParams().width = getTextViewHeight(tvPrecautionPoint4.getWidth());

        return view;
    }

    private void init(){
        gridView = view.findViewById(R.id.gridView_symptomsFrag);
        tvPrecautionPoint1 = view.findViewById(R.id.tvPrecautionPoint1);
        tvPrecautionPoint2 = view.findViewById(R.id.tvPrecautionPoint2);
        tvPrecautionPoint3 = view.findViewById(R.id.tvPrecautionPoint3);
        tvPrecautionPoint4 = view.findViewById(R.id.tvPrecautionPoint4);
        view1 = view.findViewById(R.id.viewLinePoint1);
        view2 = view.findViewById(R.id.viewLinePoint2);
        view3 = view.findViewById(R.id.viewLinePoint3);
        view4 = view.findViewById(R.id.viewLinePoint4);
    }

    private int getTextViewHeight(float fontSize){
        int height = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.widthPixels;
        if(screenHeight>0 && screenHeight<1300){
            height = (int) ((int)(fontSize*3.5)-(fontSize/3.5));
        }
        else {
            height = (int) ((int) (fontSize*3.5)+(fontSize/2.5));
        }
        return height;

    }
}
