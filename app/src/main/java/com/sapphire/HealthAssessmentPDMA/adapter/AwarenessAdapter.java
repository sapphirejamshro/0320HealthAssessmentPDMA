package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.AwarenessBean;
import com.sapphire.HealthAssessmentPDMA.fragment.AwarenessFragment;
import com.sapphire.HealthAssessmentPDMA.helper.ExpandableHeightGridView;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import java.util.List;

public class AwarenessAdapter extends BaseAdapter {

    private Activity ctx;
    private List<AwarenessBean> awarenessBeanList;
    static Integer getHeight=0;
    private ExpandableHeightGridView gridView;
    private Boolean isFirst;

    public AwarenessAdapter(Activity ctx, List<AwarenessBean> awarenessBeanList,Boolean isFirst) {
        this.ctx = ctx;
        this.awarenessBeanList = awarenessBeanList;
        this.isFirst = isFirst;
    }

    @Override
    public int getCount() {
        return awarenessBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return awarenessBeanList.get(position);
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
            view = inflater.inflate(R.layout.custom_awareness_gridview_layout,null);
            ImageView img = view.findViewById(R.id.imgViewCustomAGL);
            final TextView imgName = view.findViewById(R.id.tvNameCustomAGL);
            gridView = AwarenessFragment.view.findViewById(R.id.gridView_awarenessFrag);

            AwarenessBean bean = awarenessBeanList.get(position);
            if(bean!=null){
                img.setImageResource(bean.getImageId());
                imgName.setText(bean.getImageName());
                if (new UserSession(ctx).getSelectedLanguage().equalsIgnoreCase("urdu")) {
                    SpannableString spannableString = new SpannableString(imgName.getText().toString());
                    spannableString.setSpan(new RelativeSizeSpan(1.4f),0,spannableString.length(),0);
                    imgName.setText(spannableString, TextView.BufferType.SPANNABLE);

                    Typeface fontUrdu = Typeface.createFromAsset(ctx.getAssets(),"notonastaliqurdu_regular.ttf");
                    imgName.setTypeface(fontUrdu);
                    imgName.setIncludeFontPadding(false);
                    imgName.setLineSpacing(0,0.7f);

                }else if (new UserSession(ctx).getSelectedLanguage().equalsIgnoreCase("sindhi")) {
                    SpannableString spannableString = new SpannableString(imgName.getText().toString());
                    spannableString.setSpan(new RelativeSizeSpan(1.5f),0,spannableString.length(),0);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    imgName.setText(spannableString, TextView.BufferType.SPANNABLE);
                    Typeface fontSindhi = Typeface.createFromAsset(ctx.getAssets(),"myriad_pro_regular.ttf");
                    imgName.setTypeface(fontSindhi);
                    imgName.setIncludeFontPadding(false);
                    imgName.setLineSpacing(0,0.85f);

                }
            }
            if (awarenessBeanList.size()==5 && position == (awarenessBeanList.size()-1) && isFirst){
                    /*imgName.post(new Runnable() {
                    @Override
                    public void run() {
                        getHeight =  imgName.getHeight();
                        imgName.getLayoutParams().height = getHeight;
                        imgName.getLayoutParams().height = getTextViewHeight(imgName.getTextSize());
                        gridView.setAdapter(new AwarenessAdapter(ctx, AwarenessFragment.awarenessBeanList,false));
                    }
                });*/
                imgName.post(new Runnable() {
                    @Override
                    public void run() {
//                        getHeight =  imgName.getHeight();

                        gridView.setAdapter(new AwarenessAdapter(ctx, AwarenessFragment.awarenessBeanList, false));
                    }
                });

            }
            if (!isFirst){
                imgName.getLayoutParams().height = getHeight;
                //if(getHeight==0){
               // imgName.getLayoutParams().height = getTextViewHeight(imgName.getTextSize());

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

    public String getName(int position){
        return awarenessBeanList.get(position).getImageName();
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
