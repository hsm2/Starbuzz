package com.example.harishmanikantan.starbuzz;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LastStop extends AppCompatActivity {

    private static final String POSITION = "position";
    private static final String INITIAL = "https://developer.cumtd.com/api/v2.2/json/";
    private static final String KEY = "2032b10e0825495598792803910e0907";

    BusNames busNames;
    BusStop busStop;

    private TextView textView;

    private RequestQueue queue;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_stop);

        int id = getIntent().getExtras().getInt(POSITION);
        busNames = new BusNames();
        String vehicleID = busNames.getTripID(id);
        context=this;
        textView=(TextView) findViewById(R.id.next_stop);
        Log.d("Sup","hey");
        textView.setText("sup");
        Log.d("vehicle_id",vehicleID);
        queue = Volley.newRequestQueue(this);
        displayLastStop(vehicleID);
    }

    public void displayLastStop(String id) {
        Log.d("url",INITIAL+"GetStopTimesByTrip?trip_id="+id+"&key="+KEY);
        StringRequest request = new StringRequest(Request.Method.GET, INITIAL+"GetStopTimesByTrip?trip_id="+id+"&key="+KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("stop_times");
                    String stop_id=jsonArray.getJSONObject(0).getJSONObject("stop_point").getString("stop_name");
                    busStop = new BusStop(stop_id,context);
                    String stop_name=busStop.getStopName();
                    textView.setText(stop_name);
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
