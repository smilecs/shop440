package com.shop440;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shop440.Adapters.CategoryAdapter;
import com.shop440.Models.CategoryModel;
import com.shop440.Models.StoreModel;
import com.shop440.Api.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewItemCategoryActivity extends AppCompatActivity {
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    ArrayList<CategoryModel> model;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CategoryAdapter categoryAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        StoreModel storeModel = (StoreModel) getIntent().getSerializableExtra("backtrack");
        model = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, model, storeModel);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetCategories();
                //Refresh("1");
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoryAdapter);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        GetCategories();
    }

    private void GetCategories(){
        refreshLayout.setRefreshing(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Urls.BASE_URL + Urls.GETCATEGORIES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                refreshLayout.setRefreshing(false);
                for(int i = 0; i < response.length(); i++){
                    try{
                        JSONObject jsonObject = response.getJSONObject(i);
                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setSlug(jsonObject.getString("Slug"));
                        categoryModel.setName(jsonObject.getString("Name"));
                        model.add(categoryModel);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //model.add(store);
                    //mainAdapter.notifyDataSetChanged();
                }
                categoryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
               refreshLayout.setRefreshing(false);
                //Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_SHORT);

            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }
}

