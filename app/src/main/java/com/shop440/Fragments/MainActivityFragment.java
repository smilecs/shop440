package com.shop440.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.ProductAdapter;
import com.shop440.Models.ProductModel;
import com.shop440.R;
import com.shop440.Utils.EndlessRecyclerViewScrollListener;
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
    ProductAdapter mainAdapter;
    ArrayList<ProductModel> model;
    Context c;
    ProgressBar bar;
    String TAG = "MainActivityFragment";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;
    View view;
    String page = "1";
    TextView feedback;
    StaggeredGridLayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    Boolean next = true;

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
        mainAdapter = new ProductAdapter(c, model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
                );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                GetData("1");
                //Refresh("1");
            }
        });
        list = (RecyclerView) view.findViewById(R.id.recyclerView);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        feedback = (TextView) view.findViewById(R.id.feedback);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        list.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, String.valueOf(page));
                if(next){
                    GetData(String.valueOf(page));
                }

            }
        });
        GetData("1");
        return view;
    }

    private void GetData(String page){

        feedback.setVisibility(View.GONE);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Urls.BASE_URL + Urls.GETPRODUCTS + "?p=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    refreshLayout.setRefreshing(false);
                    bar.setVisibility(View.GONE);
                    JSONArray array = response.getJSONArray("Data");
                    next = response.getJSONObject("Page").getBoolean("Next");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        ProductModel store = new ProductModel();
                        store.setName(object.getString("Name"));
                        store.setDescription(object.getString("Description"));
                        store.setPrice(object.getString("Price"));
                        store.setCategory(object.getString("Category"));
                        store.setCity(object.getString("City"));
                        store.setCitySlug(object.getString("CitySlug"));
                        store.setOwner(object.getJSONObject("Store").getString("Name"));
                        store.setOwnerSlug(object.getJSONObject("Store").getString("Slug"));
                        store.setOwnerLogo(object.getJSONObject("Store").getString("Logo"));
                        store.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        store.setImage(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try{
                            store.setPlaceholder(placeholder[1]);
                            //Log.d(TAG, placeholder[1]);

                        }catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                            store.setPlaceholder(" ");

                        }
                        model.add(store);
                    }
                    mainAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                    Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                bar.setVisibility(View.GONE);
                feedback.setVisibility(View.VISIBLE);
                Snackbar.make(view, "Oops! Connectivity problems", Snackbar.LENGTH_LONG).show();

            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        //GetData("1");
    }
}
