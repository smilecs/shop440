package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shop440.Models.ProductModel;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.FileCache;
import com.shop440.Utils.VolleySingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductView extends AppCompatActivity {
    //ProductAdapter mainAdapter;
    ArrayList<ProductModel> model;
    ProgressBar bar;
    String TAG = "ProductView";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    Context c;
    String token;
    FileCache fileCache;
    ProductModel productModel;
    //StaggeredGridLayoutManager layoutManager;
    Boolean next = true;
    @BindView(R.id.productImage) NetworkImageView imageView;
    @BindView(R.id.productName) TextView productName;
    @BindView(R.id.productDescription) TextView productDesc;
    @BindView(R.id.productPrice) TextView productPrice;
    @BindView(R.id.productTags) TextView productTags;
    //@BindView(R.id.recyclerView) RecyclerView list;
    @BindView(R.id.storeName) TextView storeName;
    @OnClick(R.id.download) void Download(){
        try{
            downloadImage(productModel.getImage());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @OnClick(R.id.vistStore) void visit(){
        Intent i = new Intent(c, Store.class);
        StoreModel storeModel = new StoreModel();
        storeModel.setSlug(productModel.OwnerSlug);
        storeModel.setName(productModel.getOwner());
        storeModel.setLogo(productModel.getOwnerLogo());
        i.putExtra("data", storeModel);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c = this;
        ButterKnife.bind(this);
        fileCache = new FileCache(this);
        productModel = (ProductModel) getIntent().getSerializableExtra("data");
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        model = new ArrayList<>();
        getSupportActionBar().setTitle(productModel.getName());
        //mainAdapter = new ProductAdapter(this, model);
        //list = (RecyclerView) findViewById(R.id.recyclerView);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        /*layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        list.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(next){
                    GetData(String.valueOf(page));
                }

            }
        });*/
        Typeface robotMedium = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        Typeface robotBold = Typeface.createFromAsset(getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        productPrice.setTypeface(robotBold);
        productName.setTypeface(robotMedium);
        productDesc.setTypeface(robotCondensed);
        productName.setText(productModel.getName());
        productDesc.setText(productModel.getDescription());
        productPrice.setText(productModel.getPrice());
        storeName.setText(productModel.getOwner());
        byte[] imageByte = Base64.decode(productModel.getPlaceholder(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        imageView.setImageBitmap(bit);
        imageView.setImageUrl(productModel.getImage(), imageLoader);

        /*
        imageLoader.get(productModel.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });*/

        //GetData("1");


    }

    /*private void GetData(String page){

        String query = "";
        Uri tempUri = Uri.parse(productModel.getCategory());
        try{
            query = URLEncoder.encode(query, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        //feedback.setVisibility(View.GONE);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Urls.BASE_URL + Urls.GETPRODUCTS +  "?category=" + query, null, new Response.Listener<JSONObject>() {
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
                        product.setOwner(object.getJSONObject("Store").getString("Name"));
                        product.setOwnerSlug(object.getJSONObject("Store").getString("Slug"));
                        product.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        product.setOwnerLogo(object.getJSONObject("Store").getString("Logo"));
                        product.setImage(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try{
                            product.setPlaceholder(placeholder[1]);
                        }catch (Exception e){
                            e.printStackTrace();
                            product.setPlaceholder("");
                        }
                        model.add(product);
                    }
                    mainAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                bar.setVisibility(View.GONE);
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }*/

    public String downloadImage(String Imageurl) throws MalformedURLException
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(c, "Saving image.......", Toast.LENGTH_LONG).show();
            }
        });
        String type = "jpg";
        final File file = fileCache.getFile(productModel.getName(), type);
        final URL url = new URL(Imageurl);
        Thread tm = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    long startTime = System.currentTimeMillis();
                    URLConnection ucon = null;
                    ucon = url.openConnection();
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is, 5 * 1024);
                    FileOutputStream outStream = new FileOutputStream(file);
                    byte[] buff = new byte[5 * 1024];

                    //Read bytes (and store them) until there is nothing more to read(-1)
                    int len;
                    while ((len = inStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }
                    outStream.flush();
                    outStream.close();
                    inStream.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c, "Product Image saved to shop440" +  " " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        tm.start();
        Log.d(TAG, file.getAbsolutePath());
        return "ok";
    }


}
