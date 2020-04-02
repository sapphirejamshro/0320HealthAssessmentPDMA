package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.SymptomsBean;
import com.sapphire.HealthAssessmentPDMA.fragment.SymptomsFragment;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.lang.reflect.TypeVariable;
import java.util.List;

public class SymptomsAdapter extends BaseAdapter {

    private Activity ctx;
    private List<SymptomsBean> symptomsBeanList;
    public static Integer getHeight = 0;
    private ExpandableHeightGridView gridView;
    private Boolean isFirst;
    private String selectedLanguage = "";
    public SymptomsAdapter(Activity ctx, List<SymptomsBean> symptomsBeanList, Boolean isFirst) {
        this.ctx = ctx;
        this.symptomsBeanList = symptomsBeanList;
        this.isFirst = isFirst;
        selectedLanguage = new UserSession(ctx).getSelectedLanguage();
    }

    @Override
    public int getCount() {
        return symptomsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return symptomsBeanList.get(position);
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
            view = inflater.inflate(R.layout.custom_symtoms_layout,null);
            ImageView img = view.findViewById(R.id.imgViewCustomSymptoms);
            final TextView imgName = view.findViewById(R.id.tvNameCustomSymptoms);
            gridView = SymptomsFragment.view.findViewById(R.id.gridView_symptomsFrag);

            SymptomsBean bean = symptomsBeanList.get(position);
            if(bean!=null){
                img.setImageResource(bean.getImageId());
                imgName.setText(bean.getImageName());
                if(selectedLanguage.equalsIgnoreCase("Urdu")){
                    Typeface fontUrdu = Typeface.createFromAsset(ctx.getAssets(),"notonastaliqurdu_regular.ttf");
                    imgName.setTypeface(fontUrdu);
                    imgName.setIncludeFontPadding(false);
                    imgName.setLineSpacing(0,0.65f);
                }else if(selectedLanguage.equalsIgnoreCase("Sindhi")){
                    Typeface fontSindhi = Typeface.createFromAsset(ctx.getAssets(),"sindhi_fonts.ttf");
                    imgName.setTypeface(fontSindhi);
                }else if(selectedLanguage.equalsIgnoreCase("en")){
                    Typeface fontEng = Typeface.createFromAsset(ctx.getAssets(),"myriad_pro_regular.ttf");
                    imgName.setTypeface(fontEng);
                }

            }
            if (symptomsBeanList.size()==4 && position == (symptomsBeanList.size()-1) && isFirst){
                imgName.post(new Runnable() {
                    @Override
                    public void run() {
//                        getHeight =  imgName.getHeight();

                        gridView.setAdapter(new SymptomsAdapter(ctx, SymptomsFragment.symptomsBeanList,false));
                    }
                });

            }
            if (!isFirst){
                imgName.getLayoutParams().height = getHeight;
                //if(getHeight==0){
//                imgName.getLayoutParams().height = getTextViewHeight(imgName.getTextSize());

                //}
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

    public int getName(int position){
        return symptomsBeanList.get(position).getImageName();
    }

    private int getTextViewHeight(float fontSize){
        int height = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        if(screenHeight>0 && screenHeight<1300){
            height = (int) ((int)(fontSize*3.5)-(fontSize/3.5));
        }
        else {
            height = (int) ((int) (fontSize*3.5)+(fontSize/2.5));
        }
        return height;

    }
    // Method for converting DP value to pixels
    public static int getPixelsFromDPs(Activity activity, int dps, int pos){
        Resources r = activity.getResources();
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
}
