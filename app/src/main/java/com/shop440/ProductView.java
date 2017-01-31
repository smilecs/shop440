package com.shop440;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.ProductAdapter;
import com.shop440.Models.ProductModel;
import com.shop440.Utils.EndlessRecyclerViewScrollListener;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductView extends AppCompatActivity {
    ProductAdapter mainAdapter;
    ArrayList<ProductModel> model;
    ProgressBar bar;
    String TAG = "ProductView";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String token;
    ProductModel productModel;
    StaggeredGridLayoutManager layoutManager;
    Boolean next = true;
    @BindView(R.id.productImage) ImageView imageView;
    @BindView(R.id.productName) TextView productName;
    @BindView(R.id.productDescription) TextView productDesc;
    @BindView(R.id.productPrice) TextView productPrice;
    @BindView(R.id.productTags) TextView productTags;
    @BindView(R.id.recyclerView) RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        productModel = (ProductModel) getIntent().getSerializableExtra("data");
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        model = new ArrayList<>();
        mainAdapter = new ProductAdapter(this, model);
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
        productName.setText(productModel.getName());
        productDesc.setText(productModel.getDescription());
        productPrice.setText(productModel.getPrice());
        byte[] imageByte = Base64.decode(productModel.getPlaceholder(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        imageView.setImageBitmap(bit);
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        imageLoader.get(productModel.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        GetData("1");


    }

    private void GetData(String page){
        if(page.equals("1")){
            model.clear();
        }
        //feedback.setVisibility(View.GONE);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Urls.BASE_URL + Urls.GETPRODUCTS +  "?category=" + productModel.getCategory(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //refreshLayout.setRefreshing(false);
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
                //feedback.setVisibility(View.VISIBLE);
                //Snackbar.make(view, "Error Getting Results", Snackbar.LENGTH_SHORT);

            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

}
