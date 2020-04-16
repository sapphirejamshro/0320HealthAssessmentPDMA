package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.OtherUserListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherAssessmentFragment;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OtherUserListingRVAdapter extends RecyclerView.Adapter<OtherUserListingRVAdapter.OtherUserListingRVAdaperViewHolder> {

    private Context context;
    private List<OtherUserListingRVAdapterBean> beanList;
    private OtherUserListClick otherUserClickListener;
    private long lastClickTime = 0;

    public OtherUserListingRVAdapter(Context context, List<OtherUserListingRVAdapterBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    @NonNull
    @Override
    public OtherUserListingRVAdaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.other_user_listing_rv_adapter_item,parent,false);
        return new OtherUserListingRVAdaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherUserListingRVAdaperViewHolder holder, final int position) {
        int sNo = position+1;

        holder.noTV.setText(""+sNo);
        holder.nameTV.setText(beanList.get(position).getName().toUpperCase());
        holder.phoneTV.setText(beanList.get(position).getPhoneNo());
        holder.llOtherUserListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-lastClickTime<2000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                AppCompatActivity activity = (AppCompatActivity) context;
                String userId = beanList.get(position).getUserId();
                new UserSession(context).addOtherUserInfo(userId,"other");
                checkAssessmentAllowedMethod(Integer.valueOf(userId),activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class OtherUserListingRVAdaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noTV, nameTV, phoneTV;
        LinearLayout llOtherUserListing;
        OtherUserListingRVAdaperViewHolder(View view){
            super(view);
            noTV  = view.findViewById(R.id.tv_no_user_listing_rv_adapter_item);
            nameTV = view.findViewById(R.id.tv_name_user_listing_rv_adapter_item);
            phoneTV = view.findViewById(R.id.tv_phone_other_user_listing_rv_adapter_item);
            llOtherUserListing = view.findViewById(R.id.llOtherUserListing);
        }


        @Override
        public void onClick(View v) {
            if(otherUserClickListener!=null && getAdapterPosition()!=RecyclerView.NO_POSITION){

                Integer userId = Integer.parseInt(beanList.get(getAdapterPosition()).getUserId());
                otherUserClickListener.onOtherUserItemClick(userId);
            }
        }
    }
    public interface OtherUserListClick{
        void onOtherUserItemClick(Integer userId);
    }

    public void setOtherUserClickListener(OtherUserListClick otherUserClickListener) {
        this.otherUserClickListener = otherUserClickListener;
    }

    private void checkAssessmentAllowedMethod(Integer userId, final AppCompatActivity activity){
        new UserService(context).checkAssessmentAllowed(userId, new VolleyCallback() {
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
                                CommonCode.updateDisplay(new OtherAssessmentFragment(),activity.getSupportFragmentManager());
                            }else if (isAllowed && !isResponded){
                                CommonCode.updateDisplay(new OtherAssessmentFragment(),activity.getSupportFragmentManager());
                            }else if (isResponded){
                                new CommonCode(context).showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is assigned to a concerned team. Please have some patience",activity.getSupportFragmentManager(),null,true);
                            }else {
                                new CommonCode(context).showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is in process, please wait for sometime.",activity.getSupportFragmentManager(),null,true);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

}
