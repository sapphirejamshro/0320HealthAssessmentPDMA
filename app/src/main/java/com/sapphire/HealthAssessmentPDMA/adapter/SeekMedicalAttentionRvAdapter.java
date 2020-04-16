package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.SeekMedicalAttentionRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class SeekMedicalAttentionRvAdapter extends RecyclerView.Adapter<SeekMedicalAttentionRvAdapter.SeekMedicalAttentionRvAdapterViewHolder>{

    private Activity context;
    private List<SeekMedicalAttentionRVAdapterBean> beanList;
    private List<String> stringList;

    private String selectedLanguage="";
    public SeekMedicalAttentionRvAdapter(Activity context, List<SeekMedicalAttentionRVAdapterBean> beanList, List<String> stringList){
        this.context = context;
        this.beanList = beanList;
        this.stringList = stringList;

        selectedLanguage = new UserSession(context).getSelectedLanguage();
    }

    @NonNull
    @Override
    public SeekMedicalAttentionRvAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(context).inflate(R.layout.rv_seek_medical_attention_adapter,parent,false);
        return new SeekMedicalAttentionRvAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeekMedicalAttentionRvAdapterViewHolder holder, int position) {

        holder.iconImgV.setImageResource(beanList.get(position).getImg());

        SpannableString spannableStringHeading = new SpannableString(stringList.get(position));
        spannableStringHeading.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableStringHeading.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.textViewName.setText(spannableStringHeading);

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class SeekMedicalAttentionRvAdapterViewHolder extends RecyclerView.ViewHolder{


        private ImageView iconImgV;
        private TextView textViewName;
        public SeekMedicalAttentionRvAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImgV = itemView.findViewById(R.id.imgViewSeekMedicalHelpAdapter);
            textViewName = itemView.findViewById(R.id.tvNameSeekMedicalHelpAdapter);
            int bottomPadding = (int) textViewName.getTextSize();
            if(selectedLanguage.equalsIgnoreCase("Urdu")){
                Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");
                textViewName.setTypeface(fontUrdu);
                textViewName.setIncludeFontPadding(false);
                textViewName.setLineSpacing(0,0.75f);
                textViewName.setTextSize(18);
                textViewName.setPadding(8,5,8,bottomPadding);
            }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                Typeface fontSindhi = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                textViewName.setTypeface(fontSindhi);
                textViewName.setLineSpacing(0,0.95f);

                textViewName.setPadding(8,8,8,bottomPadding);
            }else if(selectedLanguage.equalsIgnoreCase("en")){

                Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
                textViewName.setTypeface(fontEng);
                textViewName.setLineSpacing(0,0.95f);
               textViewName.setPadding(8,5,8,bottomPadding);
            }

        }
    }

}
