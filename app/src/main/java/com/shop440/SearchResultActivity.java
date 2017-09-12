package com.shop440;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.ProductAdapter;
import com.shop440.Models.Datum;
import com.shop440.Models.Image;
import com.shop440.Models.Location;
import com.shop440.Models.ProductModel;
import com.shop440.Models.Store;
import com.shop440.Utils.EndlessRecyclerViewScrollListener;
import com.shop440.Api.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    String query;
    String page;
    RecyclerView list;
    ProductAdapter mainAdapter;
    ArrayList<Datum> model;
    Context c;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    StaggeredGridLayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    Boolean next = true;
    TextView feedback;
    SwipeRefreshLayout view;
    String TAG = "SearchResultActivity";
    String URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        c = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query);
        if(getIntent().hasExtra("title")){
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }
        view = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        page = "1";
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData(String.valueOf(page));
            }
        });
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        model = new ArrayList<>();
        mainAdapter = new ProductAdapter(c, model);
        list = (RecyclerView) findViewById(R.id.recyclerView);
        feedback = (TextView) findViewById(R.id.feedback);
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
    }

    private void GetData(String page){
        refreshLayout.setRefreshing(true);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(getUrl(page, query), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    refreshLayout.setRefreshing(false);
                    JSONArray array = response.getJSONArray("Data");
                    next = response.getJSONObject("Page").getBoolean("Next");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Datum store = new Datum();
                        Store productStore = new Store();
                        Image image = new Image();
                        store.setName(object.getString("Name"));
                        store.setDescription(object.getString("Description"));
                        store.setPrice(object.getInt("Price"));
                        store.setCategory(object.getString("Category"));
                        store.setCity(object.getString("City"));
                        store.setCitySlug(object.getString("CitySlug"));
                        store.setSlug(object.getString("Slug"));
                        productStore.setName(object.getJSONObject("Store").getString("Name"));
                        store.setSlug(object.getJSONObject("Store").getString("Slug"));
                        productStore.setLogo(object.getJSONObject("Store").getString("Logo"));
                        productStore.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        image.setPath(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try{
                            image.setPlaceholder(placeholder[1]);
                        }catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                            image.setPlaceholder(" ");

                        }
                        try{
                            Location location = new Location();
                            JSONArray coord = object.getJSONObject("Location").getJSONArray("Coordinates");
                            List<Double> doubleList = new ArrayList<>();
                            for(int k = 0; k<coord.length(); k++){
                                doubleList.add(coord.getDouble(k));
                            }
                            location.setCoordinates(doubleList);
                            store.setLocation(location);
                        }catch (ArrayIndexOutOfBoundsException a){
                            a.printStackTrace();
                        }
                        store.setStore(productStore);
                        store.setImage(image);
                        model.add(store);
                    }

                    mainAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                    refreshLayout.setRefreshing(false);
                    Snackbar.make(view, R.string.error_results, Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                refreshLayout.setRefreshing(false);
                feedback.setVisibility(View.VISIBLE);
                Snackbar.make(view, "Oops! Connectivity problems", Snackbar.LENGTH_LONG).show();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    public String getUrl(String page, String q){
        if(getIntent().getBooleanExtra("isSearch", true)){
            try{
                URI = Urls.BASE_URL + Urls.GETPRODUCTS +"?query=" + URLEncoder.encode(q, "UTF-8") + "&p="+page;
            }catch (UnsupportedEncodingException un){
                un.printStackTrace();
            }
        }else {
            try{
                URI = Urls.BASE_URL + Urls.GETPRODUCTS +"?category=" + URLEncoder.encode(q, "UTF-8") + "&p="+page;
            }catch (UnsupportedEncodingException un){
                un.printStackTrace();
            }
        }
        return URI;
    }
}
