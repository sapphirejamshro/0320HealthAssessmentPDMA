package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.SymptomsInfoBean;

import java.util.List;

public class SymptomsInfoRVAdapter extends RecyclerView.Adapter<SymptomsInfoRVAdapter.SymptomsInfoViewHolder> {
   private Context context;
   private List<SymptomsInfoBean> beanList;
   private String selectedLanguage;

    public SymptomsInfoRVAdapter(Context context, List<SymptomsInfoBean> beanList,String selectedLanguage) {
        this.context = context;
        this.beanList = beanList;
        this.selectedLanguage = selectedLanguage;
    }

    @NonNull
    @Override
    public SymptomsInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptoms_recyclerview_layout,parent,false);
        return new SymptomsInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomsInfoViewHolder holder, int position) {
        SymptomsInfoBean bean = beanList.get(position);
        if(bean!=null){
            if(bean.getTitle()!=null){
                    holder.tvTitle.setText(bean.getTitle());
                    holder.tvTitle.setVisibility(View.VISIBLE);
                if(position==0){
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    holder.tvTitle.setLayoutParams(params);
                }
                if(bean.getTitle().equalsIgnoreCase("پھیلاؤ") || bean.getTitle().equalsIgnoreCase("ڦحلاءُ")){
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.rightToRight = R.id.mainConstraintLayout;
                    holder.tvTitle.setLayoutParams(params);
                }
                if(bean.getTitle().equalsIgnoreCase("How it Spreads")){
                    if(holder.tvTitle.getTextSize()>=70){
                        holder.tvTitle.setPadding(15,10,75,10);
                    }
                }
            }
            else{
                holder.tvTitle.setVisibility(View.GONE);
            }
            if(bean.getSubTitle()!=null){
                holder.tvSubTitle.setText(bean.getSubTitle());
                holder.tvSubTitle.setVisibility(View.VISIBLE);
            }
            else{
                holder.tvSubTitle.setVisibility(View.GONE);
            }
            if(bean.getInfoText()!=null){
                if(selectedLanguage.equalsIgnoreCase("en")){
                    holder.tvInfo.setText(bean.getInfoText());
                }
                else if(selectedLanguage.equalsIgnoreCase("Urdu") || selectedLanguage.equalsIgnoreCase("Sindhi")){
                    SpannableString string = new SpannableString("   "+bean.getInfoText());
                   /* if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                        string.setSpan(new StyleSpan(Typeface.BOLD),2,4,SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                        string.setSpan(new RelativeSizeSpan(1.7f),2,4,SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
                    }*/

                    holder.tvInfo.setText(string);
                }

            }
            if(bean.getIsBottomVisible()!=null && bean.getIsBottomVisible()){
                holder.bottomSpace.setVisibility(View.VISIBLE);
            }
            else{
                holder.bottomSpace.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class SymptomsInfoViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvSubTitle,tvInfo;
        View bottomSpace;
        ImageView imgBulletEng,imgBulletUrdu;

        public SymptomsInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvHeading_SymptomsRVLayout);
            tvSubTitle = itemView.findViewById(R.id.tvSubHeading_SymptomsRVLayout);
            tvInfo = itemView.findViewById(R.id.tvInfo_SymptomsRVLayout);
            bottomSpace = itemView.findViewById(R.id.bottomSpace);
            imgBulletEng = itemView.findViewById(R.id.imgV_bulletEnglish);
            imgBulletUrdu = itemView.findViewById(R.id.imgV_bulletUrdu);
            setCustomViews(this);
        }
    }

    private void setCustomViews(SymptomsInfoViewHolder holder) {
        if(selectedLanguage.equalsIgnoreCase("Urdu")){
            Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");
            holder.tvTitle.setTypeface(fontUrdu);
            holder.tvSubTitle.setTypeface(fontUrdu);
            holder.tvSubTitle.setIncludeFontPadding(false);
            holder.tvInfo.setTypeface(fontUrdu);
            holder.tvTitle.setBackgroundResource(R.drawable.tv_bg_urdu);
            holder.imgBulletEng.setVisibility(View.GONE);
            holder.imgBulletUrdu.setVisibility(View.VISIBLE);
            holder.tvInfo.setIncludeFontPadding(false);
            holder.tvInfo.setLineSpacing(0,0.90f);
            holder.tvInfo.setTextSize(18);

            int margin = 26;
            int padding = 0;
            if (holder.tvInfo.getTextSize() > 30){
                margin = (int) (holder.tvInfo.getTextSize()/1.5);
                padding = (int) (holder.tvInfo.getTextSize()/2.1);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.setMargins(0,margin,-50,0);
            params.gravity = Gravity.TOP;
            holder.imgBulletUrdu.setLayoutParams(params);
             holder.imgBulletUrdu.setPadding(0,(padding+margin),0,10);
            holder.tvInfo.setPadding(5,5,5,5);

        }
        else if(selectedLanguage.equalsIgnoreCase("Sindhi")){

            Typeface fontSindhi = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
            holder.tvTitle.setTypeface(fontSindhi);
            holder.tvSubTitle.setTypeface(fontSindhi);
            holder.tvInfo.setTypeface(fontSindhi);
            holder.tvTitle.setBackgroundResource(R.drawable.tv_bg_urdu);
            holder.tvInfo.setIncludeFontPadding(false);
            holder.imgBulletEng.setVisibility(View.GONE);
            holder.imgBulletUrdu.setVisibility(View.VISIBLE);
            holder.tvInfo.setTextSize(20);
            holder.tvInfo.setLineSpacing(0,1.20f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            int margin = 10;
            if (holder.tvInfo.getTextSize() > 30){
                margin = (int) (holder.tvInfo.getTextSize()/2.7);
            }

            params.setMargins(15,margin,-50,10);
            params.gravity = Gravity.TOP;
            holder.imgBulletUrdu.setLayoutParams(params);
            holder.imgBulletUrdu.setPadding(0,margin,0,10);
            holder.tvInfo.setPadding(5,10,5,10);
        }
        else if(selectedLanguage.equalsIgnoreCase("en")){
            Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
            holder.tvTitle.setTypeface(fontEng);
            holder.tvSubTitle.setTypeface(fontEng);
            holder.tvInfo.setTypeface(fontEng);
            holder.tvInfo.setIncludeFontPadding(false);
            holder.tvTitle.setIncludeFontPadding(false);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             int margin = 10;
            if (holder.tvInfo.getTextSize() > 30){
                margin = (int) (holder.tvInfo.getTextSize()/1.5);
            }
            params.setMargins(15,0,15,10);
            params.gravity = Gravity.TOP;
            holder.imgBulletEng.setLayoutParams(params);
            holder.imgBulletEng.setPadding(0,margin,0,10);
            holder.tvInfo.setPadding(5,10,5,10);

        }
    }


}
