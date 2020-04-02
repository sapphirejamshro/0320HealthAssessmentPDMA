package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.helper.MyDateFormatter;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ActionFragment extends Fragment {

    private TextView tvMessage;
    private Activity activity;
    private View view;
    private String assessmentMessage="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_action, container, false);
        this.activity = (Activity) getContext();
        init();
        if (getArguments().getString("assessmentMessage") != null) {
            assessmentMessage = getArguments().getString("assessmentMessage");
        }else {
            assessmentMessage="In Progress";
        }

        Timestamp timestamp = MyDateFormatter.stringToTimeStampWithTime(getArguments().getString("assessment_date"));
        long now = System.currentTimeMillis(); // See note below
        long then = timestamp.getTime();
        // Positive if 'now' is later than 'then',
        // negative if 'then' is later than 'now'
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now - then);
        if (minutes >= 120 && assessmentMessage != null && assessmentMessage.length()>0) {
            tvMessage.setText(assessmentMessage);
        }else {
            tvMessage.setText("In Progress");
        }
        return view;
    }

    private void init(){
        tvMessage = view.findViewById(R.id.tv_resMsg_ActionFrag);
    }
}
