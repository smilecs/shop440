package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.Metrics;
import com.shop440.Api.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView list;
    ArrayList<StoreModel> model;
    StoreModel store;
    Context c;
    String TAG = "ProfileActivity.class";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    Toolbar toolbar;
    @BindView(R.id.storesNumber) TextView storeNumber;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @OnClick(R.id.fab) void newStore(){
        Intent i = new Intent(c, NewStoreActivity.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString(Urls.TOKEN, "null");
        String image = sharedPreferences.getString(getResources().getString(R.string.profileImage), " ");
        ButterKnife.bind(this);

       // name.setText(sharedPreferences.getString(getResources().getString(R.string.username), ""));
        /*if(!image.equals(" ")){
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageDrawable(com.shop440.Utils.Image.roundedBitmapDrawable(this, bitmap));
        }*/
        model = new ArrayList<>();
        list = (RecyclerView) findViewById(R.id.storeList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Metrics.GetMetrics(list, this));
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);

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
                    //// TODO: 11/07/2017 collect image from server and save to sharedPreferences
                    editor.putString(getResources().getString(R.string.username), response.getString("Name"));
                    editor.apply();
                    JSONObject object = response.getJSONObject("Analytics");
                    storeNumber.setText(object.getString("Stores"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editor.clear().apply();
        finish();
        return super.onOptionsItemSelected(item);
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
                        store = new StoreModel();
                        store.setName(jsonArray.getJSONObject(i).getString("Name"));
                        store.setSlug(jsonArray.getJSONObject(i).getString("Slug"));
                        store.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                        store.setProductsNumber(jsonArray.getJSONObject(i).getJSONObject("Analytics").getString("Products"));
                        store.setPurchases(jsonArray.getJSONObject(i).getJSONObject("Analytics").getString("Purchases"));
                        store.setLikes(jsonArray.getJSONObject(i).getJSONObject("Analytics").getString("Likes"));
                        store.setLogo(jsonArray.getJSONObject(i).getString("Logo"));
                        model.add(store);
                    }

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
