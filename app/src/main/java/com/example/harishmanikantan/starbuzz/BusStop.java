package com.example.harishmanikantan.starbuzz;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusStop {

    private RequestQueue queue;

    private String stop_id="";
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private static final String INITIAL = "https://developer.cumtd.com/api/v2.2/json/";
    private static final String KEY = "2032b10e0825495598792803910e0907";

    public BusStop(String id, Context context){
        stop_id=id;
        queue = Volley.newRequestQueue(context);
        getData();
    }

    public void getData(){
        StringRequest request=new StringRequest(Request.Method.GET, INITIAL + "GetStop?stop_id=" + stop_id + "&key=" + KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("stops");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public String getStopName(){
        try {
            return jsonArray.getJSONObject(0).getString("stop_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
