package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.adapter.DashboardAdapter;
import com.sapphire.HealthAssessmentPDMA.adapter.PublicListingRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.DashboardBean;
import com.sapphire.HealthAssessmentPDMA.bean.OtherUserListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.bean.PublicListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DashboardFragment extends Fragment {

    public static View view;
    private Activity activity;
    private ExpandableHeightGridView gridView;
    public static List<DashboardBean> dashboardBeanList;
    private DashboardAdapter adapter;
    private CommonCode commonCode;
    private String selectedLanguage="";
    private long lastClickTime = 0;

    public DashboardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = getActivity();
        init();
        commonCode = new CommonCode(activity);
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

        adapter = new DashboardAdapter(activity,dashboardBeanList,selectedLanguage,true);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gridName = adapter.getName(position);
                if(SystemClock.elapsedRealtime()-lastClickTime<2500){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if(gridName!=null){
                    if (selectedLanguage.equalsIgnoreCase("en")) {
                        if (gridName.equalsIgnoreCase("Assessment for \nMyself")) {
                            checkAssessmentAllowedMethod(Integer.valueOf(new UserSession(activity).getUserId()));
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
                           // CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                            getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
                        }
                    }else if (selectedLanguage.equalsIgnoreCase("Sindhi")) {
                        if (gridName.equalsIgnoreCase(activity.getResources().getString(R.string.assessment_for_self_sindhi))) {
                            //Added for Testing
//                            CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                            checkAssessmentAllowedMethod(Integer.valueOf(new UserSession(activity).getUserId()));
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
                            //CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                            getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
                        }
                    }else if (selectedLanguage.equalsIgnoreCase("Urdu")) {
                        if (position == 1) {
                            //CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                            checkAssessmentAllowedMethod(Integer.valueOf(new UserSession(activity).getUserId()));
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
                            //CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                            getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
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

    private void checkAssessmentAllowedMethod(Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new UserService(activity).checkAssessmentAllowed(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Boolean isResponded= false, isAllowed = false, isAssessmentAvailable = false;
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        JSONObject jsonObject = response.getJSONObject("data");
                        if (jsonObject.length()>0){
                            if (!jsonObject.isNull("is_assessment_available") &&
                                    jsonObject.getString("is_assessment_available").equalsIgnoreCase("1")){
                                isAssessmentAvailable = true;
                            }
                            if (!jsonObject.isNull("time_to_allow_assessment") &&
                                    jsonObject.getString("time_to_allow_assessment").equalsIgnoreCase("1")){
                                isAllowed = true;
                            }
                            if (!jsonObject.isNull("is_user_responded") &&
                                    jsonObject.getString("is_user_responded").equalsIgnoreCase("1")){
                                isResponded = true;
                            }

                            if (!isAssessmentAvailable){
                                CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                            }else if (isAllowed && !isResponded){
                                CommonCode.updateDisplay(new PublicSurveyFragment(), getActivity().getSupportFragmentManager());
                            }else if (isResponded){
                                commonCode.showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is assigned to a concerned team. Please have some patience",getActivity().getSupportFragmentManager(),null,true);
                            }else {
                                commonCode.showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is in process, please wait for sometime.",getActivity().getSupportFragmentManager(),null,true);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);

                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null,false);

            }
        });
    }


    private void getUserHavingOtherAssessments(final Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final List<OtherUserListingRVAdapterBean> beanList = new ArrayList<>();

        new UserService(activity).getUsersHavingAssessments(userId,"other",new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                String publicUserId="",userType="",userMobileNumber="", userName="";
                if(response != null && response.length() > 0 && !response.isNull("statusDescription")){
                    try {
                        String status = response.getString("statusDescription");
                        if(status != null && status.length() > 0 && status.equalsIgnoreCase("Success") ){
                            JSONObject data = response.getJSONObject("data");
                            int count=0;
                            if(data != null && data.length() > 0){
                                for (int i=1; i<=data.length();i++){
                                    count++;
                                    if (!data.isNull(""+count)){
                                        JSONObject innerObj = data.getJSONObject(""+count);
                                        //for (int j=0; j<innerObj.length();j++){
                                        if (!innerObj.isNull("public_user_id")) {
                                            publicUserId = innerObj.getString("public_user_id");
                                        }
                                        if (!innerObj.isNull("name")) {
                                            userName = innerObj.getString("name");
                                        }
                                        if (!innerObj.isNull("user_type")) {
                                            userType = innerObj.getString("user_type");
                                        }
                                        if (!innerObj.isNull("mobile")){
                                            userMobileNumber = innerObj.getString("mobile");
                                        }

                                        beanList.add(new OtherUserListingRVAdapterBean(publicUserId,userName,userMobileNumber,userType));
                                    }else {
                                        i--;
                                    }

                                }
                                if (beanList.size()>0){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dataList",new Gson().toJson(beanList));
                                    Fragment fragmentOtherUserListing = new OtherUserListingFragment();
                                    fragmentOtherUserListing.setArguments(bundle);
                                    CommonCode.updateDisplay(fragmentOtherUserListing,getActivity().getSupportFragmentManager());
                                }else {
                                    CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                                }
                            }else{
                                progressDialog.dismiss();
                                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Value [] at data of type org.json.JSONArray cannot be converted to JSONObject")){
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
                        }else{
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                        }

                    }

                }else{
                    progressDialog.dismiss();
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager(),null,false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager(),null,false);
            }
        });
    }

}
