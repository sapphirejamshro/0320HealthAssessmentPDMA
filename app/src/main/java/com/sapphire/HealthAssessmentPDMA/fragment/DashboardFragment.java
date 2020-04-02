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
import com.sapphire.HealthAssessmentPDMA.adapter.DashboardAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.DashboardBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    private View view;
    private Activity activity;
    private ExpandableHeightGridView gridView;
    private List<DashboardBean> dashboardBeanList;
    private DashboardAdapter adapter;

    private String selectedLanguage="";
    public DashboardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = getActivity();
        init();
        selectedLanguage = new UserSession(activity).getSelectedLanguage();
        NavigationDrawerActivity.tvHeading.setText("MENU");
        dashboardBeanList = new ArrayList<>();
        dashboardBeanList.add(new DashboardBean("CORONA\nAWARENESS",R.mipmap.awareness_icon,R.color.appColorGreen));
        dashboardBeanList.add(new DashboardBean("Assessment for \nMyself",R.mipmap.generated_icon,R.color.self_assessment_color));
        dashboardBeanList.add(new DashboardBean("Assessment for \nOthers",R.mipmap.other_asseesment,R.color.other_assessment_color));
        dashboardBeanList.add(new DashboardBean("Track \nAssessment",R.mipmap.previous_asseesment,R.color.previous_assessment_color));

        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            dashboardBeanList = new ArrayList<>();
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.corona_awareness_sindhi),R.mipmap.awareness_icon,R.color.appColorGreen));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_self_sindhi),R.mipmap.generated_icon,R.color.self_assessment_color));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_other_sindhi),R.mipmap.other_asseesment,R.color.other_assessment_color));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.track_assessment_sindhi),R.mipmap.previous_asseesment,R.color.previous_assessment_color));

        }else if(selectedLanguage.equalsIgnoreCase("Urdu")){
                dashboardBeanList = new ArrayList<>();
                dashboardBeanList.add(new DashboardBean("   "+activity.getResources().getString(R.string.corona_awareness_urdu),R.mipmap.awareness_icon,R.color.appColorGreen));
                dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_self_urdu),R.mipmap.generated_icon,R.color.self_assessment_color));
                dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_other_urdu),R.mipmap.other_asseesment,R.color.other_assessment_color));
                dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.track_assessment_urdu),R.mipmap.previous_asseesment,R.color.previous_assessment_color));

        }else if(selectedLanguage.equalsIgnoreCase("en")){
            dashboardBeanList = new ArrayList<>();
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.corona_awareness),R.mipmap.awareness_icon,R.color.appColorGreen));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_self),R.mipmap.generated_icon,R.color.self_assessment_color));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.assessment_for_other),R.mipmap.other_asseesment,R.color.other_assessment_color));
            dashboardBeanList.add(new DashboardBean(activity.getResources().getString(R.string.track_assessment),R.mipmap.previous_asseesment,R.color.previous_assessment_color));

        }

        adapter = new DashboardAdapter(activity,dashboardBeanList,selectedLanguage);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gridName = adapter.getName(position);
                if(gridName!=null){
                    if (selectedLanguage.equalsIgnoreCase("en")) {
                        if (gridName.equalsIgnoreCase("Assessment for \nMyself")) {
                            CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                        } else if (gridName.equalsIgnoreCase("Track \nAssessment")) {
                            Fragment publicListing = new PublicListingFragment();
                            Bundle b = new Bundle();
                            b.putString("preScreen", "Dashboard");
                            publicListing.setArguments(b);
                            CommonCode.updateDisplay(publicListing, getActivity().getSupportFragmentManager());
//                        CommonCode.updateDisplay(new InformationTabFragment(),getActivity().getSupportFragmentManager());
                        } else if (position == 0) {
                            CommonCode.updateDisplay(new AwarenessFragment(), getActivity().getSupportFragmentManager());
                        } else if (gridName.equalsIgnoreCase("Assessment for \nOthers")) {
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                        }
                    }else if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
                        if (gridName.equalsIgnoreCase(activity.getResources().getString(R.string.assessment_for_self_sindhi))) {
                            CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                        } else if (gridName.equalsIgnoreCase(activity.getResources().getString(R.string.track_assessment_sindhi))) {
                            Fragment publicListing = new PublicListingFragment();
                            Bundle b = new Bundle();
                            b.putString("preScreen", "Dashboard");
                            publicListing.setArguments(b);
                            CommonCode.updateDisplay(publicListing, getActivity().getSupportFragmentManager());
//                        CommonCode.updateDisplay(new InformationTabFragment(),getActivity().getSupportFragmentManager());
                        } else if (gridName.equalsIgnoreCase(activity.getResources().getString(R.string.corona_awareness_sindhi))) {
                            CommonCode.updateDisplay(new AwarenessFragment(), getActivity().getSupportFragmentManager());
                        } else if (gridName.equalsIgnoreCase(activity.getResources().getString(R.string.assessment_for_other_sindhi))) {
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                        }
                    }else if (selectedLanguage.equalsIgnoreCase("Urdu")) {
                        if (position == 1) {
                            CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                        } else if (position == 3) {
                            Fragment publicListing = new PublicListingFragment();
                            Bundle b = new Bundle();
                            b.putString("preScreen", "Dashboard");
                            publicListing.setArguments(b);
                            CommonCode.updateDisplay(publicListing, getActivity().getSupportFragmentManager());
//                        CommonCode.updateDisplay(new InformationTabFragment(),getActivity().getSupportFragmentManager());
                        } else if (position == 0) {
                            CommonCode.updateDisplay(new AwarenessFragment(), getActivity().getSupportFragmentManager());
                        } else if (position == 2) {
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LockDrawer)getActivity()).unLockDrawer();
    }

    private void init() {
        gridView = view.findViewById(R.id.gridView_dashboard);
    }
}
