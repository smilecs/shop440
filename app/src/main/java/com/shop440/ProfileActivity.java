package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.shop440.Adapters.StoreAdapter;
import com.shop440.Models.StoreModel;
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

public class ProfileActivity extends AppCompatActivity {
    RecyclerView list;
    StoreAdapter storeAdapter;
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
    @BindView(R.id.profile)
    ImageView imageView;
    @BindView(R.id.storesNumber)
    TextView storeNumber;
    @BindView(R.id.likeNumber) TextView lkeNumber;
    @BindView(R.id.purchaseNumber) TextView purchaseNumber;
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
        Typeface robotMedium = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface robotThin = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Thin.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(c.getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("ProfileActivity");
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString(Urls.INSTANCE.getTOKEN(), "null");
        String Image = sharedPreferences.getString(getResources().getString(R.string.profileImage), " ");
        ButterKnife.bind(this);
        name.setTypeface(robotMedium);
        name.setText(sharedPreferences.getString(getResources().getString(R.string.username), " "));
        getSupportActionBar().setTitle(sharedPreferences.getString(getResources().getString(R.string.username), " "));
        if(!Image.equals(" ")){
            byte[] bytes = Base64.decode(Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }
        lkeNumber.setTypeface(robotMedium);
        purchaseNumber.setTypeface(robotMedium);
        storeNumber.setTypeface(robotMedium);
        model = new ArrayList<>();
        list = (RecyclerView) findViewById(R.id.storeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        storeAdapter = new StoreAdapter(this, model);
        list.setAdapter(storeAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        GetProfile();
        Get_Stores();

    }

    public void GetProfile(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getME(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d(TAG, response.toString());
                    name.setText(response.getString("Name"));
                    editor.putString(getResources().getString(R.string.username), response.getString("Name"));
                    editor.apply();
                    //editor.commit();
                    //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getMyStores(), null, new Response.Listener<JSONObject>() {


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
                    storeAdapter.notifyDataSetChanged();

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
