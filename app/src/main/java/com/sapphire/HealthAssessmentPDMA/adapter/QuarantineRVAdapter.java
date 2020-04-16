package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class QuarantineRVAdapter extends RecyclerView.Adapter<QuarantineRVAdapter.QuarantineItemRVAdapterViewHolder>{

    private Context context;
    private List<String> beanList;
    private String selectedLanguage="";
    public QuarantineRVAdapter(Context context, List<String> beanList){
        this.context = context;
        this.beanList = beanList;
        selectedLanguage = new UserSession(context).getSelectedLanguage();
    }

    @NonNull
    @Override
    public QuarantineItemRVAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.quarantine_item_rv_adapter_item,parent,false);

        return new QuarantineItemRVAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuarantineItemRVAdapterViewHolder holder, int position) {

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

    public class QuarantineItemRVAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView descripTV;
        private ImageView iconImgV;
        public QuarantineItemRVAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
             descripTV = itemView.findViewById(R.id.tv_text_QuarantineItem_rv_adapter_item);
            //iconImgV = itemView.findViewById(R.id.imgV_icon_QuarantineItem_rv_adapter_item);

            if(selectedLanguage.equalsIgnoreCase("Urdu")){
                Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");
                descripTV.setTypeface(fontUrdu);
                descripTV.setIncludeFontPadding(false);
                descripTV.setLineSpacing(0,0.90f);
            }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                Typeface fontSindhi = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                descripTV.setTypeface(fontSindhi);
                descripTV.setLineSpacing(0,0.85f);
                /*descripTV.setPadding(5,5,5,38);*/
            }else if(selectedLanguage.equalsIgnoreCase("en")){
                Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                descripTV.setTypeface(fontEng);
            }
        }
    }

}
