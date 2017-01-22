package com.shop440.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.MainAdapter;
import com.shop440.Models.Store;
import com.shop440.R;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    RecyclerView list;
    MainAdapter mainAdapter;
    ArrayList<Store> model;
    Context c;
    ProgressBar bar;
    String TAG = "MainActivityFragment.class";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;
    View view;

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
        view = inflater.inflate(R.layout.fragment_main, container, false);
        list = (RecyclerView) view.findViewById(R.id.recyclerView);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        GetData();
        return view;
    }

    private void GetData(){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Urls.BASE_URL + Urls.GETSTORE, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    bar.setVisibility(View.GONE);
                    JSONArray array = response.getJSONArray("Data");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Store store = new Store();
                        store.setName(object.getString("Name"));
                        store.setDescription(object.getString("Description"));
                        store.setPrice(object.getString("Price"));
                        store.setCategory(object.getString("Category"));
                        store.setCity(object.getString("City"));
                        store.setCitySlug(object.getString("CitySlug"));
                        store.setOwner(object.getJSONObject("Store").getString("Name"));
                        store.setOwnerSlug(object.getJSONObject("Store").getString("Slug"));
                        store.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        store.setImage(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try{
                            store.setPlaceholder(placeholder[1]);

                        }catch (Exception e){

                        }
                        model.add(store);
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                bar.setVisibility(View.GONE);
                Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_SHORT);

            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }
}
