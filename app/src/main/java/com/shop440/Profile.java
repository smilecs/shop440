package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.AdapterProfile;
import com.shop440.Models.Store;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity {
    RecyclerView list;
    AdapterProfile adapterProfile;
    ArrayList<Store> model;
    Store store;
    Context c;
    String TAG = "Profile.class";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;
    Toolbar toolbar;
    @BindView(R.id.profile)
    ImageView imageView;
    @BindView(R.id.storesNumber)
    TextView storeNumber;
    @BindView(R.id.likeNumber) TextView lkeNumber;
    @BindView(R.id.purchaseNumber) TextView purchaseNumber;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @OnClick(R.id.fab) void newStore(){
        Intent i = new Intent(c, NewStore.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c = this;
        Typeface robotMedium = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface robotThin = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Thin.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(c.getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        ButterKnife.bind(this);
        name.setTypeface(robotMedium);
        lkeNumber.setTypeface(robotMedium);
        purchaseNumber.setTypeface(robotMedium);
        storeNumber.setTypeface(robotMedium);
        model = new ArrayList<>();
        list = (RecyclerView) findViewById(R.id.storeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        adapterProfile = new AdapterProfile(this, model);
        list.setAdapter(adapterProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        GetProfile();
        Get_Stores();

    }

    public void GetProfile(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.ME, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d(TAG, response.toString());
                    name.setText(response.getString("Name"));
                    toolbar.setTitle(response.getString("Name"));
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    JSONObject object = response.getJSONObject("Analytics");
                    storeNumber.setText(object.getString("Stores"));
                    purchaseNumber.setText(object.getString("Purchases"));
                    lkeNumber.setText(object.getString("Likes"));
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void Get_Stores(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.MyStores, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("Stores");
                    for(int i = 0; i < jsonArray.length(); i++){
                        store = new Store();
                        store.setName(jsonArray.getJSONObject(i).getString("Name"));
                        store.setLogo(jsonArray.getJSONObject(i).getString("Logo"));
                        model.add(store);
                    }
                    adapterProfile.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("X-AUTH-TOKEN", token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
