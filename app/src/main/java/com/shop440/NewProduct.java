package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Adapters.GalleryAdapter;
import com.shop440.Models.CategoryModel;
import com.shop440.Models.ProductModel;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.Image;
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
import butterknife.OnClick;

public class NewProduct extends AppCompatActivity {
    ProductModel productModel;
    GalleryAdapter galleryAdapter;
    private Uri fileUri;
    JSONArray jsonArray;
    JSONObject jsonObject;
    VolleySingleton volleySingleton;
    Context c;
    RequestQueue requestQueue;
    CategoryModel catModel;
    StoreModel storeModel;
    ArrayList<ProductModel> model;
    private static final int PICK_IMAGE = 1;
    SharedPreferences sharedPreferences;
    String token;
    LinearLayoutManager layoutManager;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.description) EditText description;
    @BindView(R.id.price) EditText price;
    @BindView(R.id.tag) EditText tags;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar bar;
    @BindView(R.id.lay) LinearLayout layout;
    @BindView(R.id.feedback) TextView feedback;
    @OnClick(R.id.add) void AddImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Prevent crash if no app can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    @OnClick(R.id.next) void next(){
        bar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        productModel.setName(name.getText().toString());
        productModel.setDescription(description.getText().toString());
        productModel.setPrice(price.getText().toString());
        String[] tagTmp = tags.getText().toString().split(" ");
        productModel.setTags(tagTmp);
        Log.d("data", productModel.getTags().toString());
        try{
            jsonObject.put("Name", productModel.getName());
            jsonObject.put("Description", productModel.getDescription());
            jsonObject.put("Price", Integer.valueOf(productModel.getPrice()));
            jsonObject.put("Tags", new JSONArray(productModel.getTags()));
            jsonObject.put("Category", catModel.getSlug());
            jsonObject.put("RawImages", jsonArray.toString());

        }catch (JSONException j){
            j.printStackTrace();
        }

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getADDPRODUCT() + productModel.getShop() + "/add", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent i = new Intent(c, com.shop440.Store.class);
                i.putExtra("data", storeModel);
                i.putExtra("reload", true);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                feedback.setText("Network error check connectivity");
                bar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("X-AUTH-TOKEN", token);
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(9000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        c = this;
        storeModel = (StoreModel) getIntent().getSerializableExtra("backtrack");
        productModel = new ProductModel();
        productModel.setShop(storeModel.getSlug());
        catModel = (CategoryModel) getIntent().getSerializableExtra("data");
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Urls.INSTANCE.getTOKEN(), "null");
        jsonArray = new JSONArray();
        jsonObject = new JSONObject();
        model = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(model);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(galleryAdapter);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.getData() != null) {
            Log.d("working", "true");
            //model.setType("2");
            fileUri = data.getData();
            Image image = new Image(this, fileUri);
            String base = Base64.encodeToString(image.bitmapToByteArray(image.getBitmapFromUri()), Base64.DEFAULT);
            ProductModel forImage = new ProductModel();
            forImage.setImage(base);
            jsonArray.put(base);
            model.add(forImage);
            galleryAdapter.notifyDataSetChanged();
        }

    }

}
