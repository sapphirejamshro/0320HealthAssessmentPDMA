package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sapphire.HealthAssessmentPDMA.BuildConfig;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.AssessmentAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.AssessmentBean;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.webService.LocationService;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AssessmentFragment extends Fragment {

    private RecyclerView recyclerView;

    private Activity activity;
    private View view;
    private CommonCode commonCode;
    private AssessmentAdapter assessmentAdapter;
    private List<AssessmentBean> assessmentBeanList;
    private String flightNo="", passportNo="",countryId="", countryName="", contactPersonName="",contactPersonMobNo="";
    private ProgressDialog progressDialog;
    private  JSONArray jsonArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assessment, container, false);
        activity = (Activity)getContext();
        commonCode = new CommonCode(activity);
        progressDialog = new ProgressDialog(activity);
        assessmentBeanList = new ArrayList<>();
        init();

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        if (getArguments()!=null) {
            flightNo = getArguments().getString("flightNo");
            countryId = getArguments().getString("countryId");
            passportNo = getArguments().getString("passportNo");
            contactPersonName = getArguments().getString("contactPersonName");
            contactPersonMobNo = getArguments().getString("contactedPersonMobNo");
            countryName = countryId;
            try {
                jsonArray = new JSONArray(getArguments().getString("questionsArr"));
                getAssessmentQuestions(jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return   view;

    }
    private void init(){
        recyclerView = view.findViewById(R.id.rvAssessmentFrag);
    }

    private void getAssessmentQuestions(JSONArray jsonArray){
        if (jsonArray != null && jsonArray.length()>0){
            try {

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.length()>0){
                        String question = "",answer="";
                        if (!jsonObject.isNull("question_en") &&
                                jsonObject.getString("question_en").length()>0){
                            question = jsonObject.getString("question_en");

                        }else if (!jsonObject.isNull("question_urdu") &&
                                jsonObject.getString("question_urdu").length()>0){
                            question = jsonObject.getString("question_urdu");

                        }else if (!jsonObject.isNull("question_sindhi") &&
                                jsonObject.getString("question_sindhi").length()>0){
                            question = jsonObject.getString("question_sindhi");

                        }if (!jsonObject.isNull("answer") &&
                                jsonObject.getString("answer").length()>0){
                            answer = jsonObject.getString("answer");
                            JSONObject ansJson = new JSONObject(answer);
                            int ansCount=0;
                            for (int j=0; j<ansJson.length(); j++){
                                if (!ansJson.isNull(""+ansCount)){
                                    if (j == 0) {
                                        answer =ansJson.getString(""+ansCount);
                                    }else {
                                        answer +=", "+ansJson.getString(""+ansCount);
                                    }
                                    ansCount++;
                                }else{
                                    j--;
                                }
                            }


                           /* answer = answer.replaceAll("\\[:|\\]", "");
                            String answerArr[] = answer.split(":",-1);
                            answer = answerArr[1];*/

                        }
                        assessmentBeanList.add(new AssessmentBean(question,answer,flightNo,countryName,passportNo,contactPersonName,contactPersonMobNo));

                    }
                }
                assessmentAdapter = new AssessmentAdapter(activity,assessmentBeanList);
                recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity).build());
                recyclerView.setAdapter(assessmentAdapter);
               if(Build.VERSION.SDK_INT<=22){
                   recyclerView.setNestedScrollingEnabled(false);
               }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAllCountries() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new LocationService(activity).getAllCountries(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response!=null){
                    try {
                        if(!response.isNull("data") && !response.getString("data").equalsIgnoreCase("")){
                            JSONObject dataObject = response.getJSONObject("data");
                            if(dataObject!=null && dataObject.length()>0){

                                for(int i=0;i<dataObject.length();i++){
                                    String key = String.valueOf((i+1));
                                    if(!dataObject.isNull(key) && dataObject.getString(key)!=null && !dataObject.getString(key).equalsIgnoreCase("")){
                                        if (countryId != null && countryId.length()>0
                                        && key.equalsIgnoreCase(countryId)){
                                            countryName = dataObject.getString(key);

                                            progressDialog.dismiss();
                                            break;
                                        }
                                    }
                                } // end of for Loop
                            }

                        }

                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please try again!",getActivity().getSupportFragmentManager());

                    }
                }

            }

            @Override
            public void onError(VolleyError error) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                commonCode.showErrorORSuccessAlertWithBack(activity,"error","Something went wrong, please check your internet connection.",getActivity().getSupportFragmentManager());

            }
        });
    }
}
