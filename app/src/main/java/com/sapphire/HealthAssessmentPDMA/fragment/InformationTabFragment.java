package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.webService.GetAssessmentsDetailsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InformationTabFragment extends Fragment {

    private View view;
    private Activity activity;
    private TabLayout tabLayout;
    private ScrollView mainScrollView;
    private Bundle userBundle,assessmentBundle,questionBundle;
    String userObj="", assessmentObj="", questionArr="",assessmentMessage="", flightNo="",
            passportNo="",countryId="", contactPersonName="",contactPersonMobNo="",assessmentDate="";
    private Fragment viewUserDetailsFragment,viewAssessmentsFragment,actionFragment;

    public InformationTabFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_information_tab, container, false);
        init();
        NavigationDrawerActivity.tvHeading.setText("INFORMATION");
        userBundle = new Bundle();
        questionBundle = new Bundle();
        assessmentBundle = new Bundle();
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Assessment"));
        tabLayout.addTab(tabLayout.newTab().setText("Action"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorHeight(0);

        userObj = getArguments().getString("userObj");
        assessmentObj = getArguments().getString("assessmentObj");
        questionArr = getArguments().getString("questionsArr");
        assessmentMessage = getArguments().getString("assessmentMessage");
        flightNo = getArguments().getString("flightNo");
        countryId = getArguments().getString("countryId");
        passportNo = getArguments().getString("passportNo");
        contactPersonName = getArguments().getString("contactPersonName");
        contactPersonMobNo = getArguments().getString("contactedPersonMobNo");
        assessmentDate = getArguments().getString("assessment_date");

        viewUserDetailsFragment = new ViewUserDetailsFragment();
        viewAssessmentsFragment = new AssessmentFragment();
        actionFragment = new ActionFragment();

        userBundle.putString("userObj",userObj);

        questionBundle.putString("questionsArr",questionArr);
        questionBundle.putString("countryId",countryId);
        questionBundle.putString("flightNo",flightNo);
        questionBundle.putString("passportNo",passportNo);
        questionBundle.putString("contactPersonName",contactPersonName);
        questionBundle.putString("contactedPersonMobNo",contactPersonMobNo);

        assessmentBundle.putString("assessmentObj",assessmentObj);
        assessmentBundle.putString("assessmentMessage",assessmentMessage);
        assessmentBundle.putString("assessment_date",assessmentDate);

        viewUserDetailsFragment.setArguments(userBundle);
        updateFragment(viewUserDetailsFragment);
        //getAssessmentDetailsByAssessmentId(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedTab = tab.getText().toString();
                if(selectedTab.equalsIgnoreCase("Information")){
                    updateFragment(viewUserDetailsFragment);
                }else if(selectedTab.equalsIgnoreCase("Assessment")){
                    viewAssessmentsFragment.setArguments(questionBundle);
                    updateFragment(viewAssessmentsFragment);
                }else if(selectedTab.equalsIgnoreCase("Action")){
                    actionFragment.setArguments(assessmentBundle);
                    updateFragment(actionFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void init() {
        tabLayout = view.findViewById(R.id.tabLayout_infoTab);
        mainScrollView = view.findViewById(R.id.scrollView_infoTab);
    }

    private void updateFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.tabs_content_infoTab,fragment);
        ft.commit();
    }


}
