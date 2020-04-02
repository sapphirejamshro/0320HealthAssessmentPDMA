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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationService {

    private Activity context;
    private RequestHandler requestHandler;
    private ProgressDialog progressDialog;
    private final String USERNAME = "COVIDPUBLIC", PASSWORD="Sapphire@786";

    public LocationService(Activity context) {
        this.context = context;
        requestHandler = RequestHandler.getInstance(context);
        progressDialog = new ProgressDialog(context);
    }

    public void getDistricts(final VolleyCallback callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_LOCATION_URL, null, new Response.Listener<JSONObject>() {
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
        requestHandler.addToRequestQueue(jsonObjectRequest,"getDistricts");
    }

    public void getTehsilsByDistrict(String districtId, final VolleyCallback callback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("district_id",districtId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_LOCATION_URL, jsonObject, new Response.Listener<JSONObject>() {
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
        requestHandler.addToRequestQueue(jsonObjectRequest,"getTehsilsByDistrict");
    }

    // Added By Hina Hussain on 24-March-2020
    public void getAllCountries(final VolleyCallback callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_COUNTRIES, null,
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
        requestHandler.addToRequestQueue(jsonObjectRequest,"requestGetCountries");

    }
}
