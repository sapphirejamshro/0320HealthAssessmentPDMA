package com.sapphire.HealthAssessmentPDMA.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.bean.BottomGridviewBean;

import java.util.List;

public class BottomGridviewAdapter extends BaseAdapter {

    private Activity ctx;
    private List<BottomGridviewBean> bottomBeanList;
    static Integer getHeight;
    //private ExpandableHeightGridView gridView;
    private Boolean isFirst;

    public BottomGridviewAdapter(Activity ctx, List<BottomGridviewBean> bottomBeanList, Boolean isFirst) {
        this.ctx = ctx;
        this.bottomBeanList = bottomBeanList;
        this.isFirst = isFirst;
    }

    @Override
    public int getCount() {
        return bottomBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return bottomBeanList.get(position);
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
            view = inflater.inflate(R.layout.custom_bottom_gridview_layout,null);
            final Button img = view.findViewById(R.id.imgViewCustomBottomGv);
            //gridView = NavigationDrawerActivity.view.findViewById(R.id.gridView_Bottom);

            BottomGridviewBean bean = bottomBeanList.get(position);
            if(bean!=null){
                img.setBackgroundResource(bean.getImg());
            }
            /*if (bottomBeanList.size()==4 && position == (bottomBeanList.size()-1) && isFirst){
                img.post(new Runnable() {
                    @Override
                    public void run() {
                        getHeight =  img.getHeight();
                        NavigationDrawerActivity.bottomGridview.setAdapter(new BottomGridviewAdapter(ctx, NavigationDrawerActivity.bottomGridviewBeanList,false));
                    }
                });

            }
            if (!isFirst){
                img.getLayoutParams().height = getHeight;
                //if(getHeight==0){
                img.getLayoutParams().height = getTextViewHeight(img.getHeight());

                //}
            }*/
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
