package com.shop440;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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


public class ProductView extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
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
    static final int MYCODE = 2345;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location location;
    private double currentLatitude;
    private double currentLongitude;
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
    @OnClick(R.id.getDirections) void GetDirecitons(){
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
        //getSupportActionBar().setTitle(productModel.getName());
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000);


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

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @TargetApi(23)
    public void RequestPerms(){
        requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                MYCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MYCODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        new Asynct().execute();
                        //SaveData();
                    }

                }else{
                   // AttachValues();
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("Getlocation", "onActivityResult");
        if(requestCode == 313){
            Log.d("Getlocation", "onActivityResult2");
            new Asynct().execute();
        }
    }


    public void SaveData(){
        Log.d("GetLocation", currentLatitude + " WORKS " + currentLongitude);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RequestPerms();
                }
            });

        }else {
            Log.d("GetLocation", currentLatitude + " WORKS2" + currentLongitude);
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                Log.d("GetLocation", currentLatitude + " WORKS3" + currentLongitude);
         //       store.setCoordinates(String.valueOf(currentLatitude)+","+String.valueOf(currentLongitude));
       //         AttachValues();

            }else{
     //           AttachValues();
            }
        }
    }

    public void StartGps(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> results = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        results.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        new Asynct().execute();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    ProductView.this,
                                    313);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        new Asynct().execute();
                        break;

                }
            }
        });
    }

    private class Asynct extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            SaveData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+String.valueOf(currentLatitude)+","+String.valueOf(currentLongitude)+"&daddr="+productModel.getCoordinates()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER );
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
    }
}
