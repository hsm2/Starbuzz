package com.example.harishmanikantan.starbuzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;

public class BusNames extends AppCompatActivity {

    private ListView BusNamesListView;
    private ListView timeRemainingListView;
    private TextView textView;

    private RequestQueue queue;
    private TopLevelActivity topLevelActivity;

    private static JSONObject jsonObject;
    private static JSONArray jsonArray;

    private static final String POSITION="position";
    private String url = "https://developer.cumtd.com/api/v2.2/json/GetDeparturesByStop?stop_id=GRGIKE&key=2032b10e0825495598792803910e0907";
    private static ArrayList<String> busList=new ArrayList<String>();
    private static ArrayList<String> timeRemainingList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_names);

        topLevelActivity=new TopLevelActivity();

        BusNamesListView=(ListView) findViewById(R.id.BusNames);
        timeRemainingListView=(ListView) findViewById(R.id.timeRemaining);
        textView=(TextView) findViewById(R.id.check);

        int id=(getIntent().getExtras().getInt(POSITION));
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Click",""+id);
                Intent intent=new Intent(BusNames.this,LastStop.class);

                intent.putExtra(POSITION, (int) id);
                startActivity(intent);
            }
        };
        BusNamesListView.setOnItemClickListener(itemClickListener);
        queue= Volley.newRequestQueue(this);
        showData(id);
    }

    public void showData(int id){
        StringRequest request=new StringRequest(Request.Method.GET, topLevelActivity.getBusNameURL(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);

                    jsonArray = jsonObject.getJSONArray("departures");
                    busList.clear();
                    timeRemainingList.clear();
                    for (int i=0;i<Math.min(jsonArray.length(),10);i++){
                        busList.add(jsonArray.getJSONObject(i).getString("headsign"));
                        timeRemainingList.add(Integer.toString(jsonArray.getJSONObject(i).getInt("expected_mins")));
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_textview,busList);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_textview,timeRemainingList);
                    BusNamesListView.setAdapter(arrayAdapter);
                    timeRemainingListView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public String getTripID(int id){
        try {
            return jsonArray.getJSONObject(id).getJSONObject("trip").getString("trip_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
