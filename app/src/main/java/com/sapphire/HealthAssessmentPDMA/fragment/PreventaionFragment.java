package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.PreventionRVAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.PreventionRVAdapterBean;

import java.util.ArrayList;
import java.util.List;


public class PreventaionFragment extends Fragment {


    private Activity activity;
    private View view;
    private RecyclerView preventionRV;
    private PreventionRVAdapter preventionRVAdapter;
    private List<PreventionRVAdapterBean> beanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preventions,container,false);
        activity = (Activity)getContext();
        init();

        preventionRV.setHasFixedSize(true);
        preventionRV.setLayoutManager(new GridLayoutManager(activity,2));

        beanList = new ArrayList<>();
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_5,R.string.dispose_tissue_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_1,R.string.cover_mouth_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_6,R.string.concern_with_dr_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_2,R.string.wash_hands_or_sanitize_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_3,R.string.keep_distance_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_7,R.string.stay_at_home_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_4,R.string.avoid_huging_text));
        beanList.add(new PreventionRVAdapterBean(R.mipmap.prevention_screen_icon_8,R.string.avoid_touching_t_zone_text));

        preventionRVAdapter = new PreventionRVAdapter(activity,beanList,false);
        preventionRV.setAdapter(preventionRVAdapter);
        preventionRVAdapter.notifyDataSetChanged();

        return view;
    }


    private void init(){
        preventionRV = view.findViewById(R.id.rv_prevention_frag);
    }
}
