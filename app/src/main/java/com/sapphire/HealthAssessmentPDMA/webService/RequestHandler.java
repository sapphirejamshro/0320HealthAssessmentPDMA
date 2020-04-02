package com.sapphire.HealthAssessmentPDMA.webService;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandler {

    private static RequestHandler requestInstance;
    private RequestQueue requestQueue;
    private Context context;

    private RequestHandler(Context context) {
        this.context = context;
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    // for outside world to get object

    public static synchronized RequestHandler getInstance(Context context){
        if(requestInstance==null){
          requestInstance = new RequestHandler(context);
        }
        return requestInstance;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){

        request.setTag(tag);
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    public void cancelRequest(String tag){
        getRequestQueue().cancelAll(tag);
    }


}
