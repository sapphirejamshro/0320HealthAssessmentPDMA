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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginService {

    private Context context;
    private RequestHandler requestHandler;
    private ProgressDialog progressDialog;
    private final String USERNAME = "COVIDPUBLIC";
    private final String PASSWORD = "Sapphire@786";

    public LoginService(Context context) {
        this.context = context;
        this.requestHandler = RequestHandler.getInstance(context);
        this.progressDialog = new ProgressDialog(context);
    }

    public void loginUser (String mobileNo,
                           final VolleyCallback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            mobileNo = mobileNo.replace("+92","0");
            jsonObject.put("mobile",mobileNo);
            //jsonObject.put("token_type",tokenType);

        } catch (Exception e) {

            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                RESTApiURLs.LOGIN_USER_URL, jsonObject, new Response.Listener<JSONObject>() {
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
        requestHandler.addToRequestQueue(jsonObjectRequest,"requestLogin");
    }
}
