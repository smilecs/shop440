package com.shop440.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.R;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class profile extends Fragment {

    @BindView(R.id.profile) ImageView imageView;
    @BindView(R.id.storesNumber) TextView storeNumber;
    @BindView(R.id.likeNumber) TextView lkeNumber;
    @BindView(R.id.purchaseNumber) TextView purchaseNumber;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.myStores) RelativeLayout mystores;
    SharedPreferences sharedPreferences;
    VolleySingleton volleySingleton;
    String token;
    RequestQueue requestQueue;
    String TAG = "profile_fragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        try{
            byte[] bytes = Base64.decode(sharedPreferences.getString(getResources().getString(R.string.profileImage), "empty"),Base64.CRLF);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        name.setText(sharedPreferences.getString(getResources().getString(R.string.username), "440"));
        GetProfile();
        Get_Stores();
        return view;
    }

    public void GetProfile(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.ME, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d(TAG, response.toString());
                    name.setText(response.getString("Name"));
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
                    JSONArray jsonArray = response.getJSONArray("Stores");
                    for(int i=0; i<jsonArray.length(); i++){
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.profile_stores_list, null);

                    }

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

}
