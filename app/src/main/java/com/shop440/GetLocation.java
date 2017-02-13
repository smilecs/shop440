package com.shop440;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetLocation extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    VolleySingleton volleySingleton;
    RequestQueue queue;
    Location location;
    SharedPreferences sharedPreferences;
    JSONObject jsonObject;
    static final int MYCODE = 2345;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String token;
    StoreModel store;
    @BindView(R.id.address) EditText Address;
    @BindView(R.id.progressBar2) ProgressBar loading;
    @BindView(R.id.textView5) TextView feedback;
    @BindView(R.id.save) Button saveButton;
    @OnClick(R.id.save) void save(){
        //feedback.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
        Address.setVisibility(View.GONE);
        if(Address.getText().length() > 4){
            loading.setVisibility(View.VISIBLE);
            feedback.setText("Accessing store service");
            StartGps();
            //SaveStore(jsonObject);
        }else {
            feedback.setText("Ensure to accept the location so we can get your current location to aid in directing your customers");

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        jsonObject = new JSONObject();
        store = (StoreModel) getIntent().getSerializableExtra("data");
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Urls.TOKEN, "null");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
       //StartGps();
        volleySingleton = VolleySingleton.getsInstance();
        queue = volleySingleton.getmRequestQueue();


    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // SaveData();
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("Getlocation", "onActivityResult");
        if(requestCode == 313){
          Log.d("Getlocation", "onActivityResult2");
            new Asynct().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MYCODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                       // new Asynct().execute();
                        AttachValues();
                    }

                }else{
                    AttachValues();
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @TargetApi(23)
    public void RequestPerms(){
        requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                MYCODE);
    }

    public void SaveData(){
        Log.d("GetLocation", currentLatitude + " WORKS " + currentLongitude);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RequestPerms();
                }
            });

        }else {
            Log.d("GetLocation", currentLatitude + " WORKS2" + currentLongitude);
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                Log.d("GetLocation", currentLatitude + " WORKS3" + currentLongitude);
                store.setCoordinates(String.valueOf(currentLatitude)+","+String.valueOf(currentLongitude));
                AttachValues();

            }else{
                AttachValues();
            }
        }
    }

    private void SaveStore(JSONObject jsonObject){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.NEWSTORE, jsonObject, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d("GetLocation", response.toString());
                Intent i = new Intent(GetLocation.this, Profile.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("X-AUTH-TOKEN", token);
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void StartGps(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> results = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        results.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        new Asynct().execute();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    GetLocation.this,
                                    313);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        new Asynct().execute();
                        break;

                }
            }
        });
    }

    private class Asynct extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            SaveData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SaveStore(jsonObject);
        }
    }

    private void AttachValues(){
        store.setCoordinates(String.valueOf(currentLatitude)+","+String.valueOf(currentLongitude));
        try{
            jsonObject.put("Name", store.getName());
            jsonObject.put("Description", store.getDescription());
            jsonObject.put("Specialisation", store.getSpecialisation());
            jsonObject.put("Phone", store.getPhone());
            jsonObject.put("Coordinates", store.getCoordinates());
            jsonObject.put("Address", Address.getText().toString());
            Log.d("GetLocation", jsonObject.toString());
        }catch (JSONException j){
            j.printStackTrace();
        }
    }


}


