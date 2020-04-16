package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.DashboardBean;
import com.sapphire.HealthAssessmentPDMA.fragment.AwarenessFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.DashboardFragment;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;

import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    private Activity ctx;
    private List<DashboardBean> dashboardBeanList;
    private String selectedLanguage;
    static Integer getHeight=0;
    private ExpandableHeightGridView gridView;
    private Boolean isFirst;

    public DashboardAdapter(Activity ctx, List<DashboardBean> dashboardBeanList,String selectedLanguage,Boolean isFirst) {
        this.ctx = ctx;
        this.dashboardBeanList = dashboardBeanList;
        this.selectedLanguage = selectedLanguage;
        this.isFirst = isFirst;
    }

    @Override
    public int getCount() {
        return dashboardBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return dashboardBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (selectedLanguage.equalsIgnoreCase("en")) {
                view = inflater.inflate(R.layout.dashboard_gridview_layout, null);
            }else {
                view = inflater.inflate(R.layout.dashboard_gridview_layout_sindhi_urdu, null);
            }

            Animation anim = AnimationUtils.loadAnimation(ctx,R.anim.left_right);

            // By default all grid items will animate together and will look like the gridview is
            // animating as a whole. So, experiment with incremental delays as below to get a
            // wave effect.
            anim.setStartOffset(position * 250);
//            anim.setStartOffset((position % dashboardBeanList.size()) * 500);

            view.setAnimation(anim);
            anim.start();
            ImageView img = view.findViewById(R.id.cat_img_DashbGridLayout);
            final TextView imgName = view.findViewById(R.id.cat_name_txt_DashbGridLayout);
            gridView = DashboardFragment.view.findViewById(R.id.gridView_dashboard);


            //ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout_DashbGridLayout);
            LinearLayout linearLayout = view.findViewById(R.id.linearLayout_DashbGridLayout);

            DashboardBean bean = dashboardBeanList.get(position);
            if(bean!=null){
                img.setImageResource(bean.getImageId());
                imgName.setText(bean.getImageName());
                if (selectedLanguage.equalsIgnoreCase("Sindhi")){
                    SpannableString spannableString = new SpannableString(imgName.getText().toString());
                    spannableString.setSpan(new RelativeSizeSpan(1.6f),0,spannableString.length(),0);
                    imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                    Typeface fontSindhi = Typeface.createFromAsset(ctx.getAssets(),"myriad_pro_regular.ttf");
                    imgName.setTypeface(fontSindhi);
                    imgName.setLineSpacing(0,0.95f);
                    linearLayout.setPadding(0,8,0,14);

                    /*SpannableString spannableString = new SpannableString(imgName.getText().toString());
                    spannableString.setSpan(new RelativeSizeSpan(1.6f),0,spannableString.length(),0);
                    spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                    Typeface fontSindhi = Typeface.createFromAsset(ctx.getAssets(),"LateefRegOT.ttf");
                    imgName.setTypeface(fontSindhi);
                    linearLayout.setPadding(0,12,0,12);*/
                }else if (selectedLanguage.equalsIgnoreCase("urdu")){
                    Typeface fontUrdu = Typeface.createFromAsset(ctx.getAssets(),"notonastaliqurdu_regular.ttf");
                    imgName.setTypeface(fontUrdu);
                    SpannableString spannableString = new SpannableString(imgName.getText().toString());
                    spannableString.setSpan(new RelativeSizeSpan(1.0f),0,spannableString.length(),0);
                    imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                    linearLayout.setPadding(0,8,0,-8);
                    if (position == 0 ){
                        imgName.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f,  ctx.getResources().getDisplayMetrics()), 0.68f);
                        linearLayout.setPadding(0,8,0,14);
                    }if (position == 3){
                        imgName.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f,  ctx.getResources().getDisplayMetrics()), 0.68f);
                        linearLayout.setPadding(0,12,0,10);
                    }

                }
                linearLayout.setBackgroundColor(linearLayout.getResources().getColor(bean.getColorId()));
            }

            // Added on 11-April
            if (dashboardBeanList.size()==4 && position == (dashboardBeanList.size()-1) && isFirst){
                imgName.post(new Runnable() {
                    @Override
                    public void run() {

                        gridView.setAdapter(new DashboardAdapter(ctx, DashboardFragment.dashboardBeanList, selectedLanguage,false));
                    }
                });

            }
            if (!isFirst){
                imgName.getLayoutParams().height = getHeight;
            }
            imgName.post(new Runnable() {
                @Override
                public void run() {
                    int h = imgName.getHeight();
                    if(isFirst && h>getHeight){
                        getHeight = h;
                    }
                }
            });

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
        return dashboardBeanList.get(position).getImageName();
    }
}
