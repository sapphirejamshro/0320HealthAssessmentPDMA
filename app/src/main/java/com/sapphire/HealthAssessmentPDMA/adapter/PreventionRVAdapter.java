package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.PreventionRVAdapterBean;

import java.util.List;

public class PreventionRVAdapter extends RecyclerView.Adapter<PreventionRVAdapter.PreventionRVAdapterViewHolder>{

    private Context context;
    private List<PreventionRVAdapterBean> beanList;
    private boolean isEnglish = false;
    public PreventionRVAdapter(Context context, List<PreventionRVAdapterBean> beanList,boolean isEnglish){
        this.context = context;
        this.beanList = beanList;
        this.isEnglish = isEnglish;
    }

    @NonNull
    @Override
    public PreventionRVAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prevention_rv_adapter_item,parent,false);
        return new PreventionRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreventionRVAdapterViewHolder holder, int position) {

        holder.descripTV.setText(beanList.get(position).getText());
        holder.iconImgV.setImageDrawable(context.getResources().getDrawable(beanList.get(position).getImg()));


        if(isEnglish){

            if(position%2 == 0){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(16,24,32,16);
                holder.mainContainerLL.setLayoutParams(layoutParams);
            }else{
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(32,24,16,16);
                holder.mainContainerLL.setLayoutParams(layoutParams);
            }

            holder.descripTV.setGravity(Gravity.START);
            holder.descripTV.setIncludeFontPadding(true);
            holder.descripTV.setTypeface(Typeface.DEFAULT_BOLD);
            holder.descripTV.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }else{
            if(position == 6 || position == 7){
                holder.mainContainerLL.setPadding(0,20,0,20);
            }
            if(position%2 == 0){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(16,16,32,16);
                holder.mainContainerLL.setLayoutParams(layoutParams);
            }else{
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(32,16,16,16);
                holder.mainContainerLL.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class PreventionRVAdapterViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout mainContainerLL;
        private TextView descripTV;
        private ImageView iconImgV;
        public PreventionRVAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mainContainerLL = itemView.findViewById(R.id.ll_main_container_prevention_adapter_item);
            descripTV = itemView.findViewById(R.id.tv_text_prevention_rv_adapter_item);
            iconImgV = itemView.findViewById(R.id.imgV_icon_prevention_rv_adapter_item);
        }
    }

}
