package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductView extends AppCompatActivity implements OnMapReadyCallback
{
    ProgressBar bar;
    String TAG = "ProductView";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    Context c;
    FileCache fileCache;
    ProductModel productModel;
    ShareLinkContent content;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    SharePhoto.Builder sharePhoto;
    SharePhoto photo;
    Boolean next = true;
    String[] latlng;
    private LatLng coord;
    private ImageLoader imageLoader;
    private Bundle bundle;
    private ProductView productView;
    MapView map;
    @BindView(R.id.shareText) TextView shareText;
    @BindView(R.id.productImage) NetworkImageView imageView;
    @BindView(R.id.productName) TextView productName;
    @BindView(R.id.productDescription) TextView productDesc;
    @BindView(R.id.productPrice) TextView productPrice;
    @BindView(R.id.productTags) TextView productTags;
    @BindView(R.id.storeName) TextView storeName;
    @BindView(R.id.shareProgress) ProgressBar progressBar;

    @OnClick(R.id.vistStore) void visit(){
        Intent i = new Intent(c, Store.class);
        StoreModel storeModel = new StoreModel();
        storeModel.setSlug(productModel.OwnerSlug);
        storeModel.setName(productModel.getOwner());
        storeModel.setLogo(productModel.getOwnerLogo());
        i.putExtra("data", storeModel);
        startActivity(i);
    }
    @OnClick(R.id.map_layout) void GetDirecitons(){
        //StartGps();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+productModel.getCoordinates()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER );
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        productView = this;
        setContentView(R.layout.activity_product_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        map = (MapView) findViewById(R.id.map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        c = this;
        ButterKnife.bind(this);
        fileCache = new FileCache(this);
        productModel = (ProductModel) getIntent().getSerializableExtra("data");
        latlng = productModel.getCoordinates().split(",");
        Log.d("coord", productModel.getCoordinates());
        coord = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]));
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        imageLoader = VolleySingleton.getsInstance().getImageLoader();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        initUi();
        sharePhoto = new SharePhoto.Builder();
        byte[] imageByte = Base64.decode(productModel.getPlaceholder(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        imageView.setImageBitmap(bit);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                //Log.d(TAG, result.getPostId());


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();

            }
        });
        Log.d(TAG, productModel.getSlug());
        content = new ShareLinkContent.Builder()
              .setContentUrl(Uri.parse("https://shop440.com/products/" + productModel.getSlug()))
              .setContentTitle(productModel.getName())
              .setImageUrl(Uri.parse(productModel.getImage()))
              .build();
        final ShareDialog shareDialog = new ShareDialog(this);
        CardView shareCard = (CardView) findViewById(R.id.shareCard);
        shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                shareDialog.show(content);
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        imageView.setImageUrl(productModel.getImage(), imageLoader);
        imageLoader.get(productModel.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                photo = sharePhoto.setBitmap(response.getBitmap()).setCaption(productModel.getName()).build();
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                map.onCreate(bundle);
                map.getMapAsync(productView);
            }
        }, 1800);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "here");
        progressBar.setVisibility(View.GONE);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

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

    private void initUi(){
        Typeface robotMedium = Typeface.createFromAsset(getAssets(),
              "fonts/Roboto-Medium.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(c.getAssets(),
              "fonts/Roboto-Thin.ttf");
        Typeface robotBold = Typeface.createFromAsset(getAssets(),
              "fonts/RobotoCondensed-Bold.ttf");
        Typeface robotThinItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        productPrice.setTypeface(robotMedium);
        productName.setTypeface(robotMedium);
        productDesc.setTypeface(robotCondensed);
        productName.setText(productModel.getName());
        productDesc.setText(productModel.getDescription());
        productPrice.setText(productModel.getPrice());
        storeName.setText(productModel.getOwner());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("here", "here");
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(coord);
        googleMap.addMarker(new MarkerOptions().position(coord).title(productModel.getShop()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
        map.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productview, menu);
        //MenuItem searchItem = menu.findItem(R.id.search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.download){
            try{
                downloadImage(productModel.getImage());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
