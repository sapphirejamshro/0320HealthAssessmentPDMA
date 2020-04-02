package com.sapphire.HealthAssessmentPDMA.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.NavigationDrawerBean;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;


public class NavigationDrawerAdapter extends BaseAdapter {


    private Context ctx;
    private List<NavigationDrawerBean> list;
    private String selectedLanguage;

    public NavigationDrawerAdapter(Context ctx, List<NavigationDrawerBean> list, String selectedLanguage) {
        this.ctx = ctx;
        this.list = list;
        this.selectedLanguage =  selectedLanguage;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (selectedLanguage.equalsIgnoreCase("en")) {
                view = inflater.inflate(R.layout.custom_navigation_listview, null);
            }else {
                view = inflater.inflate(R.layout.custom_navigation_listview_sindhi_urdu, null);
            }
            //view = inflater.inflate(R.layout.custom_navigation_listview,null);
            ImageView img = view.findViewById(R.id.imgMenu);
            TextView imgName = view.findViewById(R.id.txtMenu);
            img.setImageResource(list.get(position).getImageId());
            imgName.setText(list.get(position).getImgName());

            if (selectedLanguage.equalsIgnoreCase("Sindhi")){
                /*SpannableString spannableString = new SpannableString(imgName.getText().toString());
                spannableString.setSpan(new RelativeSizeSpan(0.f),0,spannableString.length(),0);
                imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                */Typeface fontSindhi = Typeface.createFromAsset(ctx.getAssets(),"sindhi_fonts.ttf");
                imgName.setTypeface(fontSindhi);
                /*linearLayout.setPadding(0,14,0,-8);*/
            }else if (selectedLanguage.equalsIgnoreCase("urdu")){
                Typeface fontUrdu = Typeface.createFromAsset(ctx.getAssets(),"notonastaliqurdu_regular.ttf");
                imgName.setTypeface(fontUrdu);
                /*SpannableString spannableString = new SpannableString(imgName.getText().toString());
                spannableString.setSpan(new RelativeSizeSpan(1.0f),0,spannableString.length(),0);
                imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                linearLayout.setPadding(0,8,0,-8);
                if (position == 0 ){
                    imgName.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f,  ctx.getResources().getDisplayMetrics()), 0.68f);
                    linearLayout.setPadding(0,8,0,14);
                }if (position == 3){
                    imgName.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f,  ctx.getResources().getDisplayMetrics()), 0.68f);
                    linearLayout.setPadding(0,12,0,10);
                }*/

            }else {
                Typeface fontEng = Typeface.createFromAsset(ctx.getAssets(),"myriad_pro_regular.ttf");
                imgName.setTypeface(fontEng);
            }
        }

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getName(int position){
        return list.get(position).getImgName();
    }
}

