package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.PublicListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.bean.UserSpecificListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.fragment.UserSpecificListingFragment;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.SelfAssessmentQuestionService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PublicListingRVAdapter extends RecyclerView.Adapter<PublicListingRVAdapter.PublicListingRVAdapterViewHolder> {


    private Context context;
    private List<PublicListingRVAdapterBean> beanList;
    private PublicListingRVItemClickListener publicListingRVItemClickListener;
    private ProgressDialog progressDialog;

    public PublicListingRVAdapter(Context context,List<PublicListingRVAdapterBean> beanList){
        this.context = context;
        this.beanList = beanList;
        progressDialog = new ProgressDialog(context);
    }



    @NonNull
    @Override
    public PublicListingRVAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.public_listing_rv_adapter_item,parent,false);
        return new PublicListingRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicListingRVAdapterViewHolder holder, final int position) {
        PublicListingRVAdapterBean bean = beanList.get(position);
        if(bean!=null){

            holder.nameTV.setText(bean.getName());
            //holder.surveryNoTV.setText("SURVEY "+beanList.get(position).getSurveryNo());
            holder.dateTV.setText(bean.getDate());
            if(bean.getResult().equalsIgnoreCase("1")){
                holder.resutlTV.setText("SCREENING NEEDED");
               holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.red_color));
               holder.resultImageView.setImageResource(R.mipmap.need_screening_icon);
               holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_bg_red);
               holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));
                holder.surveryNoTV.setBackgroundResource(R.color.red_color);
                holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));
                holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.red_color));

            }else if(bean.getResult().equalsIgnoreCase("0")){
                holder.resutlTV.setText("NO ACTION");
                //holder.resutlTV.setBackgroundResource(R.drawable.result_bg_green_public_listing_rv_adapter_item);
                holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.appColorGreen));
                holder.resultImageView.setImageResource(R.mipmap.no_action_icon);
                holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_green_bg);
                holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.left_view_green));
                holder.surveryNoTV.setBackgroundResource(R.color.green_color);
                holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
                holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
            }else if(bean.getResult().equalsIgnoreCase("IN PROGRESS")){
                holder.resutlTV.setText(bean.getResult());
                //holder.resutlTV.setBackgroundResource(R.drawable.result_bg_green_public_listing_rv_adapter_item);
                holder.resutlTV.setTextColor(holder.resutlTV.getContext().getResources().getColor(R.color.appColorGreen));
                holder.resultImageView.setImageResource(R.mipmap.no_action_icon);
                holder.mainContainerLL.setBackgroundResource(R.drawable.public_listing_green_bg);
                holder.leftSideView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.left_view_green));
                holder.surveryNoTV.setBackgroundResource(R.color.green_color);
                holder.newView2.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));
                holder.newView.setBackgroundColor(holder.leftSideView.getContext().getResources().getColor(R.color.green_color));

            }
        }
        holder.surveryNoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                if(new CommonCode(context).isNetworkAvailable()){
                    getAssessmentDetails(beanList.get(position).getUserId(),activity);
                }
                else{
                    new CommonCode(context).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",activity.getSupportFragmentManager(),null);
                }

            }
        });

       /* if(position%2 == 0){
            holder.mainContainerLL.setBackgroundColor(context.getResources().getColor(R.color.appColorWhite));
        }else {
            holder.mainContainerLL.setBackgroundColor(context.getResources().getColor(R.color.lightGreyColor));
        }*/
    }

    private void getAssessmentDetails(final String userId, final AppCompatActivity activity) {
        progressDialog.setMessage("Fetching Details...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //Integer userId = null;
        /*if(!new UserSession(context).getUserId().equalsIgnoreCase("")){
            userId = Integer.parseInt(new UserSession(context).getUserId());
        }
        final Integer finalUserId = userId;*/
        new SelfAssessmentQuestionService(context).getAssessmentDetails(Integer.valueOf(userId),new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response!=null){
                    try {
                        if(!response.isNull("data") && !response.getString("data").equalsIgnoreCase("")){
                            JSONObject dataObj = response.getJSONObject("data");
                            if(dataObj!=null && dataObj.length()>0){
                                if(userId!=null && userId.length()>0){
                                    if(!dataObj.isNull(String.valueOf(userId)) && !dataObj.getString(String.valueOf(userId)).equalsIgnoreCase("")){

                                        JSONObject userObj = dataObj.getJSONObject(String.valueOf(userId));
                                        if(userObj!=null && userObj.length()>0){
                                            ArrayList<UserSpecificListingRVAdapterBean> userSpecificList = new ArrayList<>();
                                            String userName = "";
                                            if(!userObj.isNull("name") && !userObj.getString("name").equalsIgnoreCase("")){
                                                userName = userObj.getString("name");
                                            }
                                            if(!userObj.isNull("assessment") && !userObj.getString("assessment").equalsIgnoreCase("")){
                                                JSONArray jsonArray = userObj.getJSONArray("assessment");
                                                if(jsonArray!=null && jsonArray.length()>0){
                                                    for(int i = 0;i<jsonArray.length();i++){
                                                        UserSpecificListingRVAdapterBean bean = new UserSpecificListingRVAdapterBean();
                                                        bean.setUserName(userName);
                                                        JSONObject obj = jsonArray.getJSONObject(i);
                                                        if(obj!=null){
                                                            if(!obj.isNull("assessment_id") && !obj.getString("assessment_id").equalsIgnoreCase("")){
                                                                bean.setAssessmentId(obj.getString("assessment_id"));
                                                            }

                                                            if(!obj.isNull("assessment_date") && !obj.getString("assessment_date").equalsIgnoreCase("")){
                                                                bean.setAssessmentDate(obj.getString("assessment_date"));
                                                                Timestamp timestamp = MyDateFormatter.stringToTimeStampWithTime(obj.getString("assessment_date"));
                                                                long now = System.currentTimeMillis(); // See note below
                                                                long then = timestamp.getTime();
                                                                // Positive if 'now' is later than 'then',
                                                                // negative if 'then' is later than 'now'
                                                                long minutes = TimeUnit.MILLISECONDS.toMinutes(now - then);
                                                                if (minutes >= 120){
                                                                    if(!obj.isNull("screening_needed") && !obj.getString("screening_needed").equalsIgnoreCase("")){
                                                                        bean.setAssessmentResult(obj.getString("screening_needed"));
                                                                    }
                                                                }else {
                                                                    bean.setAssessmentResult("IN PROGRESS");
                                                                }
                                                            }

                                                            userSpecificList.add(bean);
                                                        }
                                                    } // end of for Loop
                                                  // send data to UserSpecificListingFragment
                                                    if(progressDialog.isShowing()){
                                                        progressDialog.dismiss();
                                                    }
                                                    UserSpecificListingFragment userFragment = new UserSpecificListingFragment();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putParcelableArrayList("UserSpecificList", userSpecificList);
                                                    bundle.putString("userName",userName);
                                                    userFragment.setArguments(bundle);
                                                    CommonCode.updateDisplay(userFragment,activity.getSupportFragmentManager());

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        new CommonCode(context).showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",activity.getSupportFragmentManager(),null);

                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                new CommonCode(context).showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",activity.getSupportFragmentManager(),null);

            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class PublicListingRVAdapterViewHolder extends  RecyclerView.ViewHolder{

        TextView nameTV,surveryNoTV,dateTV,resutlTV;
        LinearLayout mainContainerLL;
        View leftSideView,newView,newView2;
        ImageView resultImageView;

        PublicListingRVAdapterViewHolder(View view){
            super(view);
            if(view != null){
                nameTV = view.findViewById(R.id.tvName_public_listing_RVLayout);
                surveryNoTV = view.findViewById(R.id.tv_survey_public_listing_RVLayout);
                dateTV = view.findViewById(R.id.tvDate_public_listing_RVLayout);
                resutlTV = view.findViewById(R.id.tv_result_text_public_listing_RVLayout);
                mainContainerLL = view.findViewById(R.id.ll_public_listing_rv_adapter_item);
                leftSideView = view.findViewById(R.id.colored_bar);
                newView2 = view.findViewById(R.id.newView2);
                newView = view.findViewById(R.id.newView);
                resultImageView = view.findViewById(R.id.tv_result_icon_public_listing_RVLayout);
                mainContainerLL.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(publicListingRVItemClickListener != null){
                            publicListingRVItemClickListener.onPublicListingRVItemClick(getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public void setPublicListingRVItemClickListener(PublicListingRVItemClickListener publicListingRVItemClickListener) {
        this.publicListingRVItemClickListener = publicListingRVItemClickListener;
    }

    public interface PublicListingRVItemClickListener{
        public void onPublicListingRVItemClick(int position);
    }
}
