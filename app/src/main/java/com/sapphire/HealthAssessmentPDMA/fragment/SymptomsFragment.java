package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.SymptomsAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.SymptomsInfoRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.SymptomsBean;
import com.sapphire.HealthAssessmentPDMA.bean.SymptomsInfoBean;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.ArrayList;
import java.util.List;

public class SymptomsFragment extends Fragment {

    private ExpandableHeightGridView gridView;
    private TextView tvPrecautionPoint1, tvPrecautionPoint2,tvPrecautionPoint3, tvPrecautionPoint4;
    private View view1, view2, view3, view4;
    public  View view;
    private Activity activity;
    public static List<SymptomsBean> symptomsBeanList;
    private SymptomsAdapter symptomsAdapter;
    private int getWidth;
    private RecyclerView recyclerView;
    private List<SymptomsInfoBean> infoBeanList;
    private SymptomsInfoRVAdapter adapter;
    private String selectedLanguage="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_symptoms, container, false);
        activity = (Activity) getContext();
        infoBeanList = new ArrayList<>();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        init();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        setUpData();
        return view;

    }



    private void init() {
        recyclerView = view.findViewById(R.id.recyclerview_SymptomsFrag);
    }

    private void setUpData() {
        String[] seekMedicalArray = null;
        String[] spreedArray = null;
        if(selectedLanguage.equalsIgnoreCase("en")){
             seekMedicalArray = getResources().getStringArray(R.array.seekMedicalAttention);
             spreedArray = getResources().getStringArray(R.array.howItSpreadList);
        }
        else if(selectedLanguage.equalsIgnoreCase("Urdu")){
            seekMedicalArray = getResources().getStringArray(R.array.seekMedicalAttentionUrdu);
            spreedArray = getResources().getStringArray(R.array.howItSpreadListUrdu);
        }
        else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
            seekMedicalArray = getResources().getStringArray(R.array.seekMedicalAttentionSindhi);
            spreedArray = getResources().getStringArray(R.array.howItSpreadListSindhi);
        }

        // for Medical Seek
         if(seekMedicalArray!=null && seekMedicalArray.length>0){
            if(selectedLanguage.equalsIgnoreCase("en")) {
                SymptomsInfoBean bean = new SymptomsInfoBean("When to seek Medical Attention",null,seekMedicalArray[0]);
                infoBeanList.add(bean);
            }
            else if(selectedLanguage.equalsIgnoreCase("Urdu")){
                SymptomsInfoBean bean = new SymptomsInfoBean("طبی توجہ کے علامات",null,seekMedicalArray[0]);
                infoBeanList.add(bean);
            }
            else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                SymptomsInfoBean bean = new SymptomsInfoBean("طبئ توجھ جون علامتون",null,seekMedicalArray[0]);
                infoBeanList.add(bean);
            }

            for(int i = 1;i<seekMedicalArray.length;i++){
                SymptomsInfoBean b = new SymptomsInfoBean(null,null,seekMedicalArray[i]);
                if(i==(seekMedicalArray.length-1)){
                    // last item
                    b.setIsBottomVisible(true);
                }
                infoBeanList.add(b);

            }
        }

       // For Spread
        if(spreedArray!=null && spreedArray.length>0){
            if(selectedLanguage.equalsIgnoreCase("en")){
                SymptomsInfoBean bean = new SymptomsInfoBean("How it Spreads",getString(R.string.howItSpreadText),spreedArray[0]);
                infoBeanList.add(bean);
            }
            else if(selectedLanguage.equalsIgnoreCase("Urdu")){
                SymptomsInfoBean bean = new SymptomsInfoBean("پھیلاؤ",getString(R.string.howItSpreadTextUrdu),spreedArray[0]);
                infoBeanList.add(bean);
            }
            else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                SymptomsInfoBean bean = new SymptomsInfoBean("ڦحلاءُ",getString(R.string.howItSpreadTextSindhi),spreedArray[0]);
                infoBeanList.add(bean);
            }

            for(int i = 1;i<spreedArray.length;i++){
                SymptomsInfoBean b = new SymptomsInfoBean(null,null,spreedArray[i]);
                if(i==(spreedArray.length-1)){
                    b.setIsBottomVisible(true);
                }
                infoBeanList.add(b);

            }
        }

        adapter = new SymptomsInfoRVAdapter(activity,infoBeanList,selectedLanguage);
        recyclerView.setAdapter(adapter);
    }

}
