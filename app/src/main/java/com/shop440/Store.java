package com.shop440;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.ProductAdapter;
import com.shop440.Models.ProductModel;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.EndlessRecyclerViewScrollListener;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Store extends AppCompatActivity {
    ProductAdapter mainAdapter;
    ArrayList<ProductModel> model;
    ProgressBar bar;
    String TAG = "StoreModel";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;
    String page = "1";
    StaggeredGridLayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    Boolean next = true;
    StoreModel store;
    @BindView(R.id.profile) ImageView imageView;
    @BindView(R.id.storesNumber) TextView productNumber;
    @BindView(R.id.likeNumber) TextView lkeNumber;
    @BindView(R.id.purchaseNumber) TextView purchaseNumber;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.recyclerView) RecyclerView list;
    @BindView(R.id.feedback) TextView feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        store = (StoreModel) getIntent().getSerializableExtra("data");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(store.getName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");
        if(getIntent().getBooleanExtra("reload", false)){
            Get_Store();
        }
        Typeface robotBold = Typeface.createFromAsset(getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        productNumber.setTypeface(robotBold);
        lkeNumber.setTypeface(robotBold);
        purchaseNumber.setTypeface(robotBold);
        productNumber.setText(store.getProductsNumber());
        lkeNumber.setText(store.getLikes());
        purchaseNumber.setText(store.getPurchases());
        name.setText(store.getName());
        description.setText(store.getDescription());
        model = new ArrayList<>();
        mainAdapter = new ProductAdapter(this, model);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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
        list = (RecyclerView) findViewById(R.id.recyclerView);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        list.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(next){
                    //GetData(String.valueOf(page));
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Store.this, Category.class);
                //i.putExtra("data", productModel);
                i.putExtra("backtrack", store);
                startActivity(i);
            }
        });

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        volleySingleton.getImageLoader().get(store.getLogo(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_online_store));

            }
        });

        GetData("1");
    }

    private void GetData(String page){
        if(page.equals("1")){
            model.clear();
        }
        feedback.setVisibility(View.GONE);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Urls.BASE_URL + Urls.GETSTOREPRODUCTS + store.getSlug() + "/products?p=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    refreshLayout.setRefreshing(false);
                    bar.setVisibility(View.GONE);
                    JSONArray array = response.getJSONArray("Data");
                    next = response.getJSONObject("Page").getBoolean("Next");
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Log.d(TAG, object.getJSONArray("Tags").toString());
                        ProductModel product= new ProductModel();
                        product.setName(object.getString("Name"));
                        product.setSlug(object.getString("Slug"));
                        product.setDescription(object.getString("Description"));
                        product.setPrice(object.getString("Price"));
                        product.setCategory(object.getString("Category"));
                        product.setCity(object.getString("City"));
                        product.setCitySlug(object.getString("CitySlug"));
                        //product.setTags();
                        product.setOwner(object.getJSONObject("Store").getString("Name"));
                        product.setOwnerSlug(object.getJSONObject("Store").getString("Slug"));
                        product.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        product.setImage(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try{
                            product.setPlaceholder(placeholder[1]);
                            //Log.d(TAG, placeholder[1]);

                        }catch (Exception e){
                            e.printStackTrace();
                            product.setPlaceholder("");
                        }
                        model.add(product);
                        mainAdapter.notifyDataSetChanged();
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
                feedback.setVisibility(View.VISIBLE);
                //Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_SHORT);

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

    public void Get_Store(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.SINGLESTORE + store.getSlug(), null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d(TAG, response.toString());
                    //progressBar.setVisibility(View.GONE);
                    //JSONArray jsonArray = response.getJSONArray("Stores");
                        StoreModel store = new StoreModel();
                        store.setName(response.getString("Name"));
                        store.setSlug(response.getString("Slug"));
                        store.setDescription(response.getString("Description"));
                        store.setProductsNumber(response.getJSONObject("Analytics").getString("Products"));
                        productNumber.setText(store.getProductsNumber());
                        store.setPurchases(response.getJSONObject("Analytics").getString("Purchases"));
                        store.setLikes(response.getJSONObject("Analytics").getString("Likes"));
                        store.setLogo(response.getString("Logo"));
                        //model.add(store);
                    //storeAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
               // progressBar.setVisibility(View.GONE);
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