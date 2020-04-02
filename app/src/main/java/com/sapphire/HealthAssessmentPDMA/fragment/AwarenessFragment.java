package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.adapter.AwarenessAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.AwarenessBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.ArrayList;
import java.util.List;

public class AwarenessFragment extends Fragment {

    private Activity activity;
    public static View view;
    private ExpandableHeightGridView gridView;
    public static List<AwarenessBean> awarenessBeanList;
    private AwarenessAdapter awarenessAdapter;
    String selectedLanguage="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_awareness, container, false);
        this.activity = (Activity) getContext();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        NavigationDrawerActivity.tvHeading.setText("AWARENESS");
        init();
        awarenessBeanList = new ArrayList<>();
        if (selectedLanguage.equalsIgnoreCase("en")) {
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.corona_novel),R.mipmap.novel_corona_virus_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.symptoms),R.mipmap.symptoms_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.prevention),R.mipmap.preventation_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.stay_safe_travel),R.mipmap.stay_safe_travel_icon));
        }else if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.corona_novel_sindh),R.mipmap.novel_corona_virus_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.symptoms_sindh),R.mipmap.symptoms_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.prevention_sindh),R.mipmap.preventation_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.stay_safe_travel_sindh),R.mipmap.stay_safe_travel_icon));

        }else if (selectedLanguage.equalsIgnoreCase("Urdu")){
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.corona_novel_urdu),R.mipmap.novel_corona_virus_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.symptoms_urdu),R.mipmap.symptoms_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.prevention_urdu),R.mipmap.preventation_icon));
            awarenessBeanList.add(new AwarenessBean(activity.getResources().getString(R.string.stay_safe_travel_urdu),R.mipmap.stay_safe_travel_icon));

        }
        awarenessAdapter  = new AwarenessAdapter(activity,awarenessBeanList,true);
        gridView.setAdapter(awarenessAdapter);
        gridView.setExpanded(true);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (selectedLanguage.equalsIgnoreCase("en")) {
                    if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.corona_novel))) {
                        CommonCode.updateDisplay(new NovelCoronaVirusDetails(), getActivity().getSupportFragmentManager());
                    } else if (awarenessAdapter.getName(position).equalsIgnoreCase("Symptoms")) {
                        CommonCode.updateDisplay(new SymptomsFragment(), getActivity().getSupportFragmentManager());
                    } else if (awarenessAdapter.getName(position).equalsIgnoreCase("Prevention")) {
                        String selectedLanguage = new UserSession(activity).getSelectedLanguage();
                        if (selectedLanguage.equalsIgnoreCase("en")) {
                            CommonCode.updateDisplay(new PreventionEngFragment(), getActivity().getSupportFragmentManager());

                        } else if (selectedLanguage.equalsIgnoreCase("Urdu")) {
                            CommonCode.updateDisplay(new PreventaionFragment(), getActivity().getSupportFragmentManager());

                        } else if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
                            CommonCode.updateDisplay(new PreventaionFragment(), getActivity().getSupportFragmentManager());
                        }
//                    CommonCode.updateDisplay(new PreventaionFragment(),getActivity().getSupportFragmentManager());
                    } else if (awarenessAdapter.getName(position).equalsIgnoreCase("Stay Safe/Travel")) {
                        CommonCode.updateDisplay(new StaySafeTravelFragment(), getActivity().getSupportFragmentManager());
                    }
                }else if (selectedLanguage.equalsIgnoreCase("Sindhi")){
                    if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.corona_novel_sindh))){
                        CommonCode.updateDisplay(new NovelCoronaVirusDetails(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.symptoms_sindh))){
                        CommonCode.updateDisplay(new SymptomsFragment(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.prevention_sindh))){
                        CommonCode.updateDisplay(new PreventaionFragment(),getActivity().getSupportFragmentManager());

//                    CommonCode.updateDisplay(new PreventaionFragment(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.stay_safe_travel_sindh))){
                        CommonCode.updateDisplay(new StaySafeTravelFragment(),getActivity().getSupportFragmentManager());
                    }
                }else if (selectedLanguage.equalsIgnoreCase("urdu")){
                    if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.corona_novel_urdu))){
                        CommonCode.updateDisplay(new NovelCoronaVirusDetails(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.symptoms_urdu))){
                        CommonCode.updateDisplay(new SymptomsFragment(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.prevention_urdu))){
                        CommonCode.updateDisplay(new PreventaionFragment(),getActivity().getSupportFragmentManager());

//                    CommonCode.updateDisplay(new PreventaionFragment(),getActivity().getSupportFragmentManager());
                    }else if (awarenessAdapter.getName(position).equalsIgnoreCase(activity.getResources().getString(R.string.stay_safe_travel_urdu))){
                        CommonCode.updateDisplay(new StaySafeTravelFragment(),getActivity().getSupportFragmentManager());
                    }
                }
            }
        });
        return view;
    }


    private void init(){
        gridView = view.findViewById(R.id.gridView_awarenessFrag);
    }

}
