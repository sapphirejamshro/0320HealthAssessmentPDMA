package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.adapter.OtherUserListingRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.OtherUserListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OtherUserListingFragment extends Fragment {

    private Activity activity;
    private View view;
    private RecyclerView userListingRV;
    private OtherUserListingRVAdapter userListingRVAdapter;
    private List<OtherUserListingRVAdapterBean> beanList;
    private String dataBundle="";
    private CardView cvRegisterNewOtherUser;
    private String publicUserId="",userType="",userMobileNumber="", userName="";
    private CommonCode commonCode;
    private long lastClickTime = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other_user_listing, container, false);
        this.activity = getActivity();
        commonCode = new CommonCode(activity);
        dataBundle = "";
        NavigationDrawerActivity.tvHeading.setText("OTHER USERS");
        init();
        beanList = new ArrayList<>();
        if (getArguments() != null){
            dataBundle = getArguments().getString("dataList");
            try {
                JSONArray jsonArray = new JSONArray(dataBundle);
                if (jsonArray != null && jsonArray.length()>0){
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (!jsonObject.isNull("userId")) {
                            publicUserId = jsonObject.getString("userId");
                        }
                        if (!jsonObject.isNull("name")) {
                            userName = jsonObject.getString("name");
                        }
                        if (!jsonObject.isNull("userType")) {
                            userType = jsonObject.getString("userType");
                        }
                        if (!jsonObject.isNull("phoneNo")){
                            userMobileNumber = jsonObject.getString("phoneNo");
                        }
                        beanList.add(new OtherUserListingRVAdapterBean(publicUserId,userName,
                                userMobileNumber,userType));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("==============date bundle "+dataBundle);


       /* beanList.add(new OtherUserListingRVAdapterBean(1,"TTT","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(2,"Hina","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(3,"Anila","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(4,"Imran","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(5,"Kamran","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(6,"Aadyan","03432342342"));
        beanList.add(new OtherUserListingRVAdapterBean(7,"Abdul Rehman","03432342342"));
*/
       setUserListingAdapter();
        cvRegisterNewOtherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonCode.updateDisplay(new OtherUserInformationFragment(), getActivity().getSupportFragmentManager());
            }
        });
        return view;
    }

    private void setUserListingAdapter() {
        userListingRVAdapter = new OtherUserListingRVAdapter(activity,beanList);
        userListingRV.setHasFixedSize(true);
        userListingRV.setLayoutManager(new LinearLayoutManager(activity));
        userListingRV.setAdapter(userListingRVAdapter);
        if(Build.VERSION.SDK_INT<=22){
            userListingRV.setNestedScrollingEnabled(false);
        }
        addListClickListener();
    }

    private void init(){
        userListingRV = view.findViewById(R.id.rv_other_user_listing_frag);
        cvRegisterNewOtherUser = view.findViewById(R.id.cv_register_new_user_container_other_user_listing_frag);
    }

    private void addListClickListener() {
        userListingRVAdapter.setOtherUserClickListener(new OtherUserListingRVAdapter.OtherUserListClick() {
            @Override
            public void onOtherUserItemClick(Integer userId) {
                if(SystemClock.elapsedRealtime()-lastClickTime<2000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new UserSession(activity).addOtherUserInfo(String.valueOf(userId),"other");
                //checkAssessmentAllowedMethod(userId);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PublicSurveyResultFragment.isComeFromOtherAssess){
            System.out.println("====come here in onresume");
            PublicSurveyResultFragment.isComeFromOtherAssess = false;
            getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
        }
    }


    // Added on 13-April-2020

    private void getUserHavingOtherAssessments(final Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
       // final List<OtherUserListingRVAdapterBean> beanList = new ArrayList<>();

        new UserService(activity).getUsersHavingAssessments(userId,"other",new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
               // String publicUserId="",userType="",userMobileNumber="", userName="";
                if(response != null && response.length() > 0 && !response.isNull("statusDescription")){
                    try {
                        String status = response.getString("statusDescription");
                        if(status != null && status.length() > 0 && status.equalsIgnoreCase("Success") ){
                            JSONObject data = response.getJSONObject("data");
                            int count=0;
                            beanList.clear();
                            if(data != null && data.length() > 0){
                                for (int i=1; i<=data.length();i++){
                                    count++;
                                    if (!data.isNull(""+count)){
                                        JSONObject innerObj = data.getJSONObject(""+count);
                                        //for (int j=0; j<innerObj.length();j++){
                                        if (!innerObj.isNull("public_user_id")) {
                                            publicUserId  = innerObj.getString("public_user_id");
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
                                    setUserListingAdapter();
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
