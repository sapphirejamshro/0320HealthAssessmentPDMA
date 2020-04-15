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
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class PreventionRVAdapter extends RecyclerView.Adapter<PreventionRVAdapter.PreventionRVAdapterViewHolder>{

    private Context context;
    private List<String> beanList;
    private boolean isEnglish = false;
    private String selectedLanguage="";
    public PreventionRVAdapter(Context context, List<String> beanList,boolean isEnglish){
        this.context = context;
        this.beanList = beanList;
        this.isEnglish = isEnglish;
        selectedLanguage = new UserSession(context).getSelectedLanguage();
    }

    @NonNull
    @Override
    public PreventionRVAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (isEnglish){
            view = LayoutInflater.from(context).inflate(R.layout.prevention_rv_adapter_item,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.prevention_rv_adapter_item_sindhi_urdu,parent,false);
        }
        return new PreventionRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreventionRVAdapterViewHolder holder, int position) {

        holder.descripTV.setText(beanList.get(position));
        if (selectedLanguage.equalsIgnoreCase("urdu")){
            holder.descripTV.setText("  "+beanList.get(position));
        }else {
            holder.descripTV.setText(beanList.get(position));
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

            if(selectedLanguage.equalsIgnoreCase("Urdu")){
                Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");
                descripTV.setTypeface(fontUrdu);
                descripTV.setIncludeFontPadding(false);
                descripTV.setLineSpacing(0,0.90f);

                int margin = 26;
                int padding = 0;
                if (descripTV.getTextSize() > 30){
                    margin = (int) (descripTV.getTextSize()/2.3);
                    padding = (int) (descripTV.getTextSize()/2.1);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                params.setMargins(0,margin,15,0);
                params.gravity = Gravity.TOP;
                iconImgV.setLayoutParams(params);
                iconImgV.setPadding(0,(padding+margin),0,10);
                descripTV.setPadding(5,5,5,5);
            }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                Typeface fontSindhi = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                descripTV.setTypeface(fontSindhi);
                descripTV.setTextSize(20);
                descripTV.setLineSpacing(0,0.95f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                int margin = 10;
                if (descripTV.getTextSize() > 30){
                    margin = (int) (descripTV.getTextSize()/2.7);
                }

                params.setMargins(15,margin,15,10);
                params.gravity = Gravity.TOP;
                iconImgV.setLayoutParams(params);
                iconImgV.setPadding(0,margin,0,10);
                descripTV.setPadding(5,10,5,10);
            }else if(selectedLanguage.equalsIgnoreCase("en")){
                Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                descripTV.setTypeface(fontEng);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                int margin = 10;
                if (descripTV.getTextSize() > 30){
                    margin = (int) (descripTV.getTextSize()/2.7);
                }

                params.setMargins(15,0,15,10);
                params.gravity = Gravity.TOP;
                iconImgV.setLayoutParams(params);
                iconImgV.setPadding(0,margin,0,10);
                descripTV.setPadding(5,10,5,10);
            }
        }
    }

}
