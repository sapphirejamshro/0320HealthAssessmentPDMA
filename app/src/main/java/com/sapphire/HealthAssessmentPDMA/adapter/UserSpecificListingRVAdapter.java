package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.UserSpecificListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.fragment.InformationTabFragment;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.GetAssessmentsDetailsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSpecificListingRVAdapter extends RecyclerView.Adapter<UserSpecificListingRVAdapter.UserSpecificPublicListingRVAdapterViewHolder> {


    private Context context;
    private ArrayList<UserSpecificListingRVAdapterBean> beanList;
    private ProgressDialog progressDialog;
    private UserSession session;

    public UserSpecificListingRVAdapter(Context context,ArrayList<UserSpecificListingRVAdapterBean> beanList){
        this.context = context;
        this.beanList = beanList;
        progressDialog = new ProgressDialog(context);
        session = new UserSession(context);
    }


    @NonNull
    @Override
    public UserSpecificPublicListingRVAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_spec_listing_rv_adapter_item,parent,false);
        return new UserSpecificPublicListingRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSpecificPublicListingRVAdapterViewHolder holder, final int position) {

        UserSpecificListingRVAdapterBean bean = beanList.get(position);
        if(bean!=null){

            Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
            Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");


            if (session.getSelectedLanguage().equalsIgnoreCase("urdu")){
                holder.nameTV.setTypeface(fontUrdu);
            }else{
                holder.nameTV.setTypeface(fontEng,Typeface.BOLD);
            }

            if(bean.getUserName()!=null){
                holder.nameTV.setText(bean.getUserName());
            }
            if(bean.getAssessmentDate()!=null){
                holder.dateTV.setText(MyDateFormatter.timestampStringToDateString(bean.getAssessmentDate()));
            }
            if(bean.getAssessmentResult()!=null){

                if(bean.getAssessmentResult().equalsIgnoreCase("1")){
                    holder.resutlTV.setText("NEED SCREENING");
                    holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.red_color));
                    holder.resultImageView.setImageResource(R.mipmap.need_screening_icon);
                    holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_bg_red);
                    holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));
                    holder.tvGetDetails.setBackgroundResource(R.color.red_color);
                    holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));
                    holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));
                }else if(bean.getAssessmentResult().equalsIgnoreCase("0")){
                    holder.resutlTV.setText("NO ACTION");
                    //holder.resutlTV.setBackgroundResource(R.drawable.result_bg_green_public_listing_rv_adapter_item);
                    holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.appColorGreen));
                    holder.resultImageView.setImageResource(R.mipmap.no_action_icon);
                    holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_green_bg);
                    holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.left_view_green));
                    holder.tvGetDetails.setBackgroundResource(R.color.green_color);
                    holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
                    holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));

                }else if(bean.getAssessmentResult().equalsIgnoreCase("IN PROGRESS")){
                    holder.resutlTV.setText(bean.getAssessmentResult());
                    //holder.resutlTV.setBackgroundResource(R.drawable.result_bg_green_public_listing_rv_adapter_item);
                    holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.appColorGreen));
                    holder.resultImageView.setImageResource(R.mipmap.no_action_icon);
                    holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_green_bg);
                    holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.left_view_green));
                    holder.tvGetDetails.setBackgroundResource(R.color.green_color);
                    holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
                    holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
                }
            }

            holder.tvGetDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Fragment userFragment = new InformationTabFragment();
                    /*Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("UserSpecificList", userSpecificList);
                    bundle.putString("userName",userName);
                    userFragment.setArguments(bundle);*/
                    AppCompatActivity activity = (AppCompatActivity) context;
                    if(new CommonCode(context).isNetworkAvailable()){
                        getAssessmentDetailsByAssessmentId(Integer.valueOf(beanList.get(position).getAssessmentId()),activity);
                    }
                    else{
                        new CommonCode(context).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",activity.getSupportFragmentManager(),null,false);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }



    public class UserSpecificPublicListingRVAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV,tvGetDetails,dateTV,resutlTV;
        LinearLayout mainContainerLL;
        View leftSideView,newView,newView2;
        ImageView resultImageView;

        UserSpecificPublicListingRVAdapterViewHolder(View view){
            super(view);
            if(view != null){
                nameTV = view.findViewById(R.id.tvName_user_spec_listing_RVLayout);
                tvGetDetails = view.findViewById(R.id.tv_survey_user_spec_listing_RVLayout);
                dateTV = view.findViewById(R.id.tvDate_user_spec_listing_RVLayout);
                resutlTV = view.findViewById(R.id.tv_result_text_user_spec_listing_RVLayout);
                mainContainerLL = view.findViewById(R.id.ll_user_spec_listing_rv_adapter_item);
                leftSideView = view.findViewById(R.id.colored_bar_user_spec_adapter_item);
                resultImageView = view.findViewById(R.id.tv_result_icon_user_spec_listing_RVLayout);
                newView2 = view.findViewById(R.id.newView2);
                newView = view.findViewById(R.id.newView);

            }

        }

    }

    private void getAssessmentDetailsByAssessmentId(final Integer assessmentId, final AppCompatActivity activity){
        progressDialog.setMessage("Fetching Details...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new GetAssessmentsDetailsService(context).getAssessmentDetailsByAssessmentId(assessmentId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        String countryId="",flightNo="",passportNo="", contactedPersonName="",assessmentDate="",
                                contactedPersonMobNo="", selected_language="",scoringStatus="";
                        if (statusDescription.length()>0 && statusDescription.equalsIgnoreCase("Success")){
                            JSONObject jsonObject = response.getJSONObject("data");
                            if (jsonObject.length()>0){
                                JSONObject userObject = jsonObject.getJSONObject("user_data");
                                if (userObject != null && userObject.length()>0){
                                    if (!userObject.isNull("country_last_visited_name")){
                                        countryId = userObject.getString("country_last_visited_name");
                                    }
                                    if (!userObject.isNull("passport_no")){
                                        passportNo = userObject.getString("passport_no");
                                    }
                                    if (!userObject.isNull("flight_no")){
                                        flightNo = userObject.getString("flight_no");
                                    }
                                    if (!userObject.isNull("selected_language")){
                                        selected_language = userObject.getString("selected_language");
                                    }

                                }

                                JSONObject assessmentObject = jsonObject.getJSONObject("assessment_data");
                                if (assessmentObject.length()>0){
                                    if (!assessmentObject.isNull("contact_name")){
                                        contactedPersonName = assessmentObject.getString("contact_name");
                                    }
                                    if (!assessmentObject.isNull("contact_mobile")){
                                        contactedPersonMobNo = assessmentObject.getString("contact_mobile");

                                    }if (!assessmentObject.isNull("assessment_date")){
                                        assessmentDate = assessmentObject.getString("assessment_date");
                                    }
                                    if (!assessmentObject.isNull("scoring_status") && assessmentObject.getString("scoring_status").length()>0){
                                        scoringStatus = assessmentObject.getString("scoring_status");
                                    }
                                }
                                System.out.println("==================scoring "+scoringStatus);
                                String assessmentMessage = jsonObject.getString("assessment_message");
                                JSONArray questionsArray = jsonObject.getJSONArray("questions_data");

                                Bundle bundle = new Bundle();
                                bundle.putString("userObj",(userObject).toString());
                                bundle.putString("assessmentObj",assessmentObject.toString());
                                bundle.putString("assessmentMessage",assessmentMessage);
                                bundle.putString("questionsArr",questionsArray.toString());
                                bundle.putString("countryId",countryId);
                                bundle.putString("flightNo",flightNo);
                                bundle.putString("passportNo",passportNo);
                                bundle.putString("contactPersonName",contactedPersonName);
                                bundle.putString("contactedPersonMobNo",contactedPersonMobNo);
                                bundle.putString("assessment_date",assessmentDate);
                                bundle.putString("scoringStatus",scoringStatus);
                                Fragment userFragment = new InformationTabFragment();
                                userFragment.setArguments(bundle);
                                CommonCode.updateDisplay(userFragment,activity.getSupportFragmentManager());
                            }
                        }else {
                            new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",activity.getSupportFragmentManager(),null,false);
                        }
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        System.out.println("=========error "+e);
                        new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",activity.getSupportFragmentManager(),null,false);

                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                new CommonCode(activity).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",activity.getSupportFragmentManager(),null,false);
            }
        });
    }

}
