package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.AssessmentBean;
import com.sapphire.HealthAssessmentPDMA.helper.CustomTypefaceSpan;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private Context context;
    private List<AssessmentBean> beanList;
    private String selectedLanguage="";


    public AssessmentAdapter(Context context, List<AssessmentBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        selectedLanguage = new UserSession(context).getSelectedLanguage();
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custon_rv_assessment,parent,false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if(selectedLanguage.equalsIgnoreCase("Urdu")){
            holder.tvQuestion.setText("   "+beanList.get(position).getQuestion());
        }
        else{
            holder.tvQuestion.setText(beanList.get(position).getQuestion());
        }

        holder.tvAnswer.setText(beanList.get(position).getAnswer());
        holder.tvFlightNoVal.setText(beanList.get(position).getFlightNo());
        holder.tvPassportNoVal.setText(beanList.get(position).getPassportNo());
        holder.tvCountryVal.setText(beanList.get(position).getCountryName());
        holder.tvContactedPersonVal.setText(beanList.get(position).getContactedPersonName());
        holder.tvContactedPersonMobNoVal.setText(beanList.get(position).getContactedPersonMobNo());

        if (position == 4){
            if (beanList.get(position).getContactedPersonName().length()>0){
                holder.tbContactedPerson.setVisibility(View.VISIBLE);
                holder.trContactedPersonName.setVisibility(View.VISIBLE);
            }
            if (beanList.get(position).getContactedPersonMobNo().length()>0){
                holder.trContactedPersonMobNo.setVisibility(View.VISIBLE);
            }
        }

        if (position == 3){
            if (beanList.get(position).getCountryName().length()>0){
                holder.tbFlight.setVisibility(View.VISIBLE);
                holder.trCountry.setVisibility(View.VISIBLE);
            }
            if (beanList.get(position).getFlightNo().length()>0){
                holder.trFlightNo.setVisibility(View.VISIBLE);
            }
            if (beanList.get(position).getPassportNo().length()>0){
                holder.trPassportNo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{

        private TextView tvQuestion, tvAnswer,tvFlightNoVal,tvPassportNoVal,tvCountryVal,
                tvFlightNoLabel,tvPassportLabel, tvCountryLabel, tvContactedPersonLabel,
                tvContactedPersonVal, tvContactedPersonMobNoLabel,tvContactedPersonMobNoVal;
        TableLayout tbFlight, tbContactedPerson;
        TableRow trFlightNo, trCountry, trPassportNo, trContactedPersonName,trContactedPersonMobNo;
        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuesAssessmentCustomLayout);
            tvAnswer = itemView.findViewById(R.id.tvAnswerAssessmentCustomLayout);
            tvFlightNoLabel = itemView.findViewById(R.id.tvFlightNumCustomAssessment);
            tvFlightNoVal = itemView.findViewById(R.id.tvFlightNumValCustomAssessment);
            tvPassportLabel = itemView.findViewById(R.id.tvPassportNumCustomAssessment);
            tvPassportNoVal = itemView.findViewById(R.id.tvPassporttNumValCustomAssessment);
            tvCountryLabel = itemView.findViewById(R.id.tvCountryCustomAssessment);
            tvCountryVal = itemView.findViewById(R.id.tvCountryValCustomAssessment);
            tvContactedPersonLabel = itemView.findViewById(R.id.tvNameCustomAssessment);
            tvContactedPersonVal = itemView.findViewById(R.id.tvNameValCustomAssessment);
            tvContactedPersonMobNoLabel = itemView.findViewById(R.id.tvMobileNumCustomAssessment);
            tvContactedPersonMobNoVal = itemView.findViewById(R.id.tvMobileNumValCustomAssessment);

            trFlightNo = itemView.findViewById(R.id.trFlightNo);
            trPassportNo = itemView.findViewById(R.id.trPassportNo);
            trCountry = itemView.findViewById(R.id.trCountry);
            trContactedPersonName = itemView.findViewById(R.id.trName);
            trContactedPersonMobNo = itemView.findViewById(R.id.trMobileNo);
            tbContactedPerson = itemView.findViewById(R.id.personDetailsLayout);
            tbFlight = itemView.findViewById(R.id.flightDetailsLayout);
            setCustomFonts(this);
        }
    }

    private void setCustomFonts(AssessmentViewHolder holder) {
        Typeface fontEng = Typeface.createFromAsset(context.getAssets(),"myriad_pro_regular.ttf");
        Typeface fontUrdu = Typeface.createFromAsset(context.getAssets(),"notonastaliqurdu_regular.ttf");
        Typeface fontSindhi = Typeface.createFromAsset(context.getAssets(),"sindhi_fonts.ttf");

        // for Country
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringCountry = new SpannableString(context.getResources().getString(R.string.country_text_sindhi));
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontSindhi),13,spanStringCountry.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvCountryLabel.setText(spanStringCountry);

        }else {
            SpannableString spanStringCountry = new SpannableString(holder.tvCountryLabel.getText().toString());
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontEng),0,12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringCountry.setSpan(new CustomTypefaceSpan("",fontUrdu),13,spanStringCountry.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvCountryLabel.setText(spanStringCountry);
        }

        // for Flight No
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringFlightNo = new SpannableString(context.getResources().getString(R.string.flight_text_sindhi));
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontEng),0,10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontSindhi),11,spanStringFlightNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvFlightNoLabel.setText(spanStringFlightNo);
        }else {
            SpannableString spanStringFlightNo = new SpannableString(holder.tvFlightNoLabel.getText().toString());
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontEng),0,10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringFlightNo.setSpan(new CustomTypefaceSpan("",fontUrdu),11,spanStringFlightNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvFlightNoLabel.setText(spanStringFlightNo);
        }

        // for Passport No
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringPassportNo = new SpannableString(context.getResources().getString(R.string.passport_no_sindhi));
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontEng),0,11, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontSindhi),12,spanStringPassportNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvPassportLabel.setText(spanStringPassportNo);
        }else {
            SpannableString spanStringPassportNo = new SpannableString(holder.tvPassportLabel.getText().toString());
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontEng),0,11, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringPassportNo.setSpan(new CustomTypefaceSpan("",fontUrdu),12,spanStringPassportNo.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvPassportLabel.setText(spanStringPassportNo);
        }

        // for Name
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringName = new SpannableString(context.getResources().getString(R.string.name_text1_sindhi));
            spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("",fontSindhi),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvContactedPersonLabel.setText(spanStringName);
        }else {
            SpannableString spanStringName = new SpannableString(holder.tvContactedPersonLabel.getText().toString());
            spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringName.setSpan(new CustomTypefaceSpan("",fontUrdu),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvContactedPersonLabel.setText(spanStringName);
        }

        // for Mobile
        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            SpannableString spanStringMobile = new SpannableString(context.getResources().getString(R.string.mobile_no1_sindhi));
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontSindhi),7,spanStringMobile.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvContactedPersonMobNoVal.setText(spanStringMobile);
        }else {
            SpannableString spanStringMobile = new SpannableString(holder.tvContactedPersonMobNoVal.getText().toString());
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanStringMobile.setSpan(new CustomTypefaceSpan("",fontUrdu),7,spanStringMobile.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvContactedPersonMobNoVal.setText(spanStringMobile);
        }

        if (selectedLanguage.equalsIgnoreCase("en")){
            holder.tvQuestion.setTypeface(fontEng);
            holder.tvAnswer.setTypeface(fontEng);
            holder.tvCountryVal.setTypeface(fontEng);
            holder.tvFlightNoLabel.setTypeface(fontEng);
            holder.tvPassportNoVal.setTypeface(fontEng);
            holder.tvContactedPersonVal.setTypeface(fontEng);
            holder.tvContactedPersonMobNoVal.setTypeface(fontEng);
        }else if (selectedLanguage.equalsIgnoreCase("sindhi")){
            holder.tvQuestion.setTypeface(fontSindhi);
            holder.tvAnswer.setTypeface(fontSindhi);
            holder.tvCountryVal.setTypeface(fontSindhi);
            holder.tvFlightNoLabel.setTypeface(fontSindhi);
            holder.tvPassportNoVal.setTypeface(fontSindhi);
            holder.tvContactedPersonVal.setTypeface(fontSindhi);
            holder.tvContactedPersonMobNoVal.setTypeface(fontSindhi);
        }else if (selectedLanguage.equalsIgnoreCase("urdu")){
            holder.tvQuestion.setTypeface(fontUrdu);
            holder.tvAnswer.setTypeface(fontUrdu);
            holder.tvCountryVal.setTypeface(fontUrdu);
            holder.tvFlightNoLabel.setTypeface(fontUrdu);
            holder.tvPassportNoVal.setTypeface(fontUrdu);
            holder.tvContactedPersonVal.setTypeface(fontUrdu);
            holder.tvContactedPersonMobNoVal.setTypeface(fontUrdu);
        }
    }
}
