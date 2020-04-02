package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.activity.NavigationDrawerActivity;
import com.sapphire.HealthAssessmentPDMA.adapter.PublicListingRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.PublicListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PublicListingFragment extends Fragment implements PublicListingRVAdapter.PublicListingRVItemClickListener  {


    private Activity activity;
    private View view;

    private RecyclerView publicListingRV;
    private List<PublicListingRVAdapterBean> beanList;
    private PublicListingRVAdapter publicListingRVAdapter;
    private CardView addNewSurveyCard;
    private boolean isItemClickable = false;
    private ProgressDialog progressDialog;
    String userIdString="", name="", assessmentDate="",screeningNeeded="",assessmentTime="";

    private CommonCode commonCode;
    private UserSession session;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_public_listing, container, false);
        this.activity =(Activity) getContext();
        session = new UserSession(activity);
        init();
        NavigationDrawerActivity.tvHeading.setText("ASSESSMENTS");
        commonCode = new CommonCode(activity);
        progressDialog = new ProgressDialog(activity);
        /*String preScreen = getArguments().getString("preScreen");
        if(preScreen != null && preScreen.length()> 0 && preScreen.equalsIgnoreCase("Dashboard")){
            isItemClickable = true;
        }else {
            isItemClickable = false;
        }
*/
        beanList = new ArrayList<>();



        addNewSurveyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
//               startActivity(new Intent(getActivity(),SignInActivity.class));
            }
        });

        return view;
    }


    private void init(){
        publicListingRV = view.findViewById(R.id.rv_public_listing_frag);
        addNewSurveyCard = view.findViewById(R.id.cv_add_new_survey_container_public_listing_frag);
    }

    @Override
    public void onPublicListingRVItemClick(int position) {
        if(isItemClickable){
            //CommonCode.updateDisplay(new UserSpecificListingFragment(),getActivity().getSupportFragmentManager());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //if(!CommonCode.isTransactionPending){
            if (commonCode.isNetworkAvailable()){
                getUserHavingAssessments(Integer.valueOf(session.getUserId()));
            }else {
                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Please Connect to Internet!",getActivity().getSupportFragmentManager());
            }
        //}
    }

    private void getUserHavingAssessments(Integer userId){
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        beanList = new ArrayList<>();

        new UserService(activity).getUsersHavingAssessments(userId,new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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
                                        userIdString = innerObj.getString("user_id");
                                            name = innerObj.getString("name");
                                            assessmentDate = MyDateFormatter.timestampStringToDateString(innerObj.getString("assessment_date"));
                                            assessmentTime = MyDateFormatter.timestampStringToTimeString(innerObj.getString("assessment_date"));
                                            screeningNeeded = innerObj.getString("screening_needed");
                                            Timestamp timestamp = MyDateFormatter.stringToTimeStampWithTime(innerObj.getString("assessment_date"));
                                            long now = System.currentTimeMillis(); // See note below
                                            long then = timestamp.getTime();
                                            // Positive if 'now' is later than 'then',
                                            // negative if 'then' is later than 'now'
                                            long minutes = TimeUnit.MILLISECONDS.toMinutes(now - then);
                                            if (minutes >= 120){
                                                beanList.add(new PublicListingRVAdapterBean(userIdString,name,0,assessmentDate,screeningNeeded));

                                            }else {
                                                beanList.add(new PublicListingRVAdapterBean(userIdString,name,0,assessmentDate,"IN PROGRESS"));

                                          //  }
                                        }
                                    }else {
                                        i--;
                                    }

                                }
                                if (beanList.size()>0){

                                    publicListingRVAdapter = new PublicListingRVAdapter(activity,beanList);
                                    publicListingRV.setLayoutManager(new LinearLayoutManager(activity));
                                    publicListingRV.setHasFixedSize(true);

                                    publicListingRV.setAdapter(publicListingRVAdapter);
                                    publicListingRVAdapter.notifyDataSetChanged();
                                   // publicListingRVAdapter.setPublicListingRVItemClickListener(PublicListingFragment.this);
                                    //}
                                    if(Build.VERSION.SDK_INT<=22){
                                        publicListingRV.setNestedScrollingEnabled(false);
                                    }
                                }
                            }else{
                                progressDialog.dismiss();
                                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());
                            }
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Value [] at data of type org.json.JSONArray cannot be converted to JSONObject")){
                            commonCode.showErrorORSuccessAlertWithBack(activity,"error","No Records Found!",getActivity().getSupportFragmentManager());
                        }else{
                            commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());
                        }

                    }

                }else{
                    progressDialog.dismiss();
                    commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());
            }
        });
    }



    private void parseJsonData(JSONObject data){
        System.out.println("---@@## getUsersHavingAssessments(), response : "+data);
    }



    private Boolean checkTimeAndDateSettings(){
        Boolean isSetAutomatic = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                if(Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1)
                {
                    isSetAutomatic = true;
                    // Toast.makeText(activity,"Enabled high Api",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Disabed
                    isSetAutomatic = false;
                    //startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS),RequestCode.DATE_SETTINGS);
                    // Toast.makeText(activity,"Date and Time are set manually",Toast.LENGTH_SHORT).show();
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                if(Settings.System.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1)
                {
                    // Enabled
                    isSetAutomatic = true;
                    //Toast.makeText(activity,"Enabled low Api",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Disabed
                    isSetAutomatic = false;
                    //startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS),RequestCode.DATE_SETTINGS);
                    //Toast.makeText(activity,"Date and Time are set manually",Toast.LENGTH_SHORT).show();
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return isSetAutomatic;
    }

}
