package com.example.harishmanikantan.starbuzz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class TopLevelActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 15;
    private RequestQueue queue;
    private TextView textView;
    private ListView bus_names_listview;
    private static ArrayList<String> listItems = new ArrayList<String>();

    private String provider;
    private static final String POSITION = "position";
    private static final String INITIAL = "https://developer.cumtd.com/api/v2.2/json/";
    private static final String KEY = "2032b10e0825495598792803910e0907";
    private static final String url2 = "https://developer.cumtd.com/api/v2.2/json/GetStopsByLatLon?lat=40.108144&lon=-88.229178&count=10&key=2032b10e0825495598792803910e0907";

    private LocationManager locationManager;
    private LocationListener locationListener;
    protected GoogleApiClient googleApiClient;
    protected Location mLastLocation;
    protected double lat;
    protected double lon;

    private Context context;

    private static JSONArray stopArr;
    private JSONObject jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        context = this;
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        textView = (TextView) findViewById(R.id.textView);
        bus_names_listview = (ListView) findViewById(R.id.stop_names);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, BusNames.class);
                intent.putExtra(POSITION, (int) id);
                startActivity(intent);
            }
        };

        bus_names_listview.setOnItemClickListener(itemClickListener);
        queue = Volley.newRequestQueue(this);

        //updateData(url2, "stops", "stop_name");
        //updateData(INITIAL+"GetStopsByLatLon?lat=40.1029840&lon=-88.2352910&count=10&key="+KEY,"stops","stop_name");
        //displayStopsByLatLon();
    }

    public void updateData(String url, final String object, final String attribute) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonResponse = new JSONObject(response);

                    stopArr = jsonResponse.getJSONArray(object);
                    //String[] stop_names=new String[10];
                    for (int i = 0; i < stopArr.length(); i++) {
                        //stop_names[i]=stopArr.getJSONObject(i).getString("stop_name");
                        listItems.add(stopArr.getJSONObject(i).getString(attribute));

                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_textview, listItems);
                    bus_names_listview.setAdapter(arrayAdapter);

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

    public void displayStopsByLatLon() {
        /*LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);*/
        updateData(url2,"stops","stop_name");
        /*if (Build.VERSION.SDK_INT<23){
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location;
            provider = locationManager.getBestProvider(criteria, false);
            location = locationManager.getLastKnownLocation(provider);
            lat=location.getLatitude();
            lon=location.getLongitude();
            Log.d("location",lat+ " "+lon);
        }*/
        //updateData(INITIAL+"GetStopsByLatLon?lat="+latitude+"&lon="+longitude+"&count=10&key="+KEY,"stops","stop_name");

        /*ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listItems);
        bus_names_listview.setAdapter(arrayAdapter);*/

    }

    public String getBusNameURL(int id){
        return INITIAL+"GetDeparturesByStop?stop_id="+getStopID(id)+"&key="+KEY;
    }
    public String getStopName(int id){
        return listItems.get(id);
    }
    public String getStopID(int id){
        try {
            return stopArr.getJSONObject(id).getString("stop_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            updateData(INITIAL+"GetStopsByLatLon?lat="+lat+"&lon="+lon+"&count=10&key="+KEY,"stops","stop_name");
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }*/
}