package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.adapter.UserSpecificListingRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.UserSpecificListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificListingFragment extends Fragment {

    private Activity activity;
    private View view;

    private RecyclerView userSpecListingRV;
    private ArrayList<UserSpecificListingRVAdapterBean> beanList;
    private UserSpecificListingRVAdapter userSpecListingRVAdapter;
    private CardView addNewSurveyCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_specific_listing, container, false);
        activity = (Activity)getContext();
        init();
        beanList = new ArrayList<>();
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            NavigationDrawerActivity.tvHeading.setText("ASSESSMENTS-"+bundle.getString("userName").toUpperCase());
            beanList =  bundle.getParcelableArrayList("UserSpecificList");
            if(beanList!=null && beanList.size()>0){
                setUserAdapter();
            }

        }
        addNewSurveyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonCode.updateDisplay(new DashboardFragment(),getActivity().getSupportFragmentManager());
            }
        });
        return view;
    }

    private void setUserAdapter() {
        userSpecListingRVAdapter = new UserSpecificListingRVAdapter(activity,beanList);
        userSpecListingRV.setLayoutManager(new LinearLayoutManager(activity));
        userSpecListingRV.setHasFixedSize(true);
        userSpecListingRV.setAdapter(userSpecListingRVAdapter);
        //userSpecListingRVAdapter.notifyDataSetChanged();
        if(Build.VERSION.SDK_INT<=22) {
            userSpecListingRV.setNestedScrollingEnabled(false);
        }
    }


    private void init(){
        userSpecListingRV = view.findViewById(R.id.rv_user_specific_listing_frag);
        addNewSurveyCard = view.findViewById(R.id.cv_add_new_survey_container_user_specific_listing_frag);
    }

}
