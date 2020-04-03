package com.sapphire.HealthAssessmentPDMA.webService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.models.UserModel;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserService {

    private Activity context;
    private RequestHandler requestHandler;
    private ProgressDialog progressDialog;
    private final String USERNAME = "COVIDPUBLIC", PASSWORD="Sapphire@786";

    public RegisterUserService(Activity context) {
        this.context = context;
        requestHandler = RequestHandler.getInstance(context);
        progressDialog = new ProgressDialog(context);
    }

    public void registerUser(UserModel userModel,String selectedLanguage, String userType,
                             String hashKey, final VolleyCallback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",userModel.getName());
            String cnic = "";
            if (userModel.getCnic().length()>0){
                cnic = userModel.getCnic().replace("-","");
            }
            // Added By Hina  on 26-March-2020
            if(userType!=null && !userType.equalsIgnoreCase("")){
                jsonObject.put("user_type",userType);
                if(!new UserSession(context).getUserId().equalsIgnoreCase("")){
                    jsonObject.put("public_user_id",Integer.parseInt(new UserSession(context).getUserId()));
                }

            }
            jsonObject.put("cnic",cnic);
            String mobileNum = userModel.getMobileNumber().replace("+92","0");
            jsonObject.put("mobile",mobileNum);
            jsonObject.put("age",userModel.getAge());
            System.out.println("===========gender "+userModel.getGender());
            jsonObject.put("gender",userModel.getGender());
            jsonObject.put("district_id",userModel.getDistrict());
            jsonObject.put("taluka_id",userModel.getTehsil());
            jsonObject.put("uc_id",userModel.getUnionCouncil());
            jsonObject.put("address",userModel.getAddress());
            jsonObject.put("latitude",userModel.getLatitude());
            jsonObject.put("longitude",userModel.getLongitude());
            jsonObject.put("selected_language",selectedLanguage);
            if (hashKey != null && hashKey.length()>0) {
                jsonObject.put("hash_key", hashKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.REGISTER_USER_URL, jsonObject, new Response.Listener<JSONObject>() {
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestHandler.addToRequestQueue(jsonObjectRequest,"registerUser");

    }


    public void verifyOTP (String mobileNo, String token,
                           final VolleyCallback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            mobileNo = mobileNo.replace("+92","0");
            jsonObject.put("mobile",mobileNo);
            jsonObject.put("otp",token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                RESTApiURLs.VERIFY_REGISTER_OTP, jsonObject, new Response.Listener<JSONObject>() {
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
                String auth = "Basic "+Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                header.put("Authorization",auth);
                return header;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestHandler.addToRequestQueue(jsonObjectRequest,"verifyOTP");
    }

}
