package com.sapphire.HealthAssessmentPDMA.webService;

import android.content.Context;
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

public class GetAssessmentsDetailsService {

    private Context context;
    private RequestHandler requestHandler;
    private final String USERNAME = "COVIDPUBLIC";
    private final String PASSWORD = "Sapphire@786";

    public GetAssessmentsDetailsService(Context context) {
        this.context = context;
        requestHandler = RequestHandler.getInstance(context);
    }

    public void getAssessmentDetailsByAssessmentId(final Integer assessmentId, final VolleyCallback callback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("assessment_id", assessmentId);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RESTApiURLs.GET_ALL_ASSESSMENTS_DETAILS,
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


