package com.shop440.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shop440.Adapters.MainAdapter;
import com.shop440.Models.Store;
import com.shop440.R;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    RecyclerView list;
    MainAdapter mainAdapter;
    ArrayList<Store> model;
    Store store;
    Context c;
    String TAG = "MainActivityFragment.class";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = getActivity();
        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        model = new ArrayList<>();
        mainAdapter = new MainAdapter(c, model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        list = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        GetData();
        return view;
    }

    private void GetData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Urls.GETSTORE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
