package com.sapphire.HealthAssessmentPDMA.webService;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfAssessmentQuestionService {
    private Context context;
    private RequestHandler requestHandler;
    private ProgressDialog progressDialog;
    private final String USERNAME = "COVIDPUBLIC";
    private final String PASSWORD = "Sapphire@786";

    public SelfAssessmentQuestionService(Context context) {
        this.context = context;
        requestHandler = RequestHandler.getInstance(context);
    }

    public void getAssessmentQuestion(final VolleyCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_ASSESSMENT_QUESTION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                String credentials = String.format("%s:%s",USERNAME,PASSWORD);
                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                header.put("Authorization",auth);
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestHandler.addToRequestQueue(jsonObjectRequest,"requestAssessmentQuestion");
    }

    public void addAssessmentData(final HashMap<String,String> questionResMap, final String countryId, final String passportNo,
                                  final String flightNo, final List<String> optionListQuest8, final String otherDiseaseName,
                                  final String personName
            , String personMobileNo,String otherUserId,String userType,
                                  final VolleyCallback callback){

        JSONObject jsonObject = new JSONObject();
        personMobileNo = personMobileNo.replace("+92","0");
        UserSession session = new UserSession(context);
        try {

            if(!session.getUserId().equalsIgnoreCase("")){
                jsonObject.put("public_user_id",Integer.parseInt(session.getUserId()));
            }
            else{
                jsonObject.put("public_user_id",0);
            }
            if(otherUserId!=null && !otherUserId.equalsIgnoreCase("")){
                jsonObject.put("other_public_user_id",Integer.parseInt(otherUserId));
            }

            jsonObject.put("user_type",userType);
            jsonObject.put("country_last_visited",countryId);
            jsonObject.put("passport_no",passportNo);
            jsonObject.put("flight_no",flightNo);
            jsonObject.put("contact_name",personName);
            jsonObject.put("contact_mobile",personMobileNo);
            JSONObject questionObj = new JSONObject();

            if(questionResMap!=null && questionResMap.size()>0){
                for(int i =1;i<=questionResMap.size();i++){

                    if(questionResMap.containsKey(String.valueOf(i))){
                        JSONObject obj = new JSONObject();
                        if (i == 6){
                            obj.put("0", questionResMap.get(String.valueOf(i)));
                            int count = i;
                            count+=1;
                            questionObj.put(String.valueOf(count), obj);
                        }else {
                            obj.put("0", questionResMap.get(String.valueOf(i)));
                            questionObj.put(String.valueOf(i), obj);
                        }
                    }
                }
                //jsonObject.put("questions_result",questionObj);
            }
            if(optionListQuest8!=null && optionListQuest8.size()>0){
                JSONObject obj =   new JSONObject();

                for(int i=0;i<optionListQuest8.size();i++){
                    if (optionListQuest8.get(i).trim().equalsIgnoreCase("Other")
                    || optionListQuest8.get(i).trim().equalsIgnoreCase("کوئی اور بيماری")
                    || optionListQuest8.get(i).trim().equalsIgnoreCase("ٻي ڪا بيماري")){
                        obj.put(String.valueOf(i),optionListQuest8.get(i));

                        obj.put("1",otherDiseaseName);
                    }else {
                        obj.put(String.valueOf(i),optionListQuest8.get(i));
                    }
                }
                questionObj.put("8",obj);

            }
            jsonObject.put("questions_result",questionObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("==================json obj "+jsonObject);
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                RESTApiURLs.ADD_ASSESSMENT_DATA, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                String credentials = String.format("%s:%s",USERNAME,PASSWORD);
                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                header.put("Authorization",auth);
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestHandler.addToRequestQueue(jsonObjectRequest,"requestAddAssessment");
    }


    public void getAssessmentDetails(final Integer userId,final VolleyCallback callback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("public_user_id", userId);

        }
        catch (JSONException e) {
                e.printStackTrace();
            }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_ASSESSMENTS_DETAILS,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                String credentials = String.format("%s:%s",USERNAME,PASSWORD);
                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                header.put("Authorization",auth);
                return header;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestHandler.addToRequestQueue(request,"requestGetAssessment");

        }


}
