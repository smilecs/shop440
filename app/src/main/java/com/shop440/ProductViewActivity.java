package com.shop440;

import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shop440.Models.ProductModel;
import com.shop440.Models.StoreModel;
import com.shop440.Utils.AppEventsLogger;
import com.shop440.Utils.FileCache;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    String TAG = "ProductViewActivity";
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    FileCache fileCache;
    ProductModel productModel;
    ShareLinkContent content;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    SharePhoto.Builder sharePhoto;
    SharePhoto photo;
    Boolean next = true;
    String[] latlng;
    MapView map;
    @BindView(R.id.shareText)
    TextView shareText;
    @BindView(R.id.productImage)
    NetworkImageView imageView;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.productDescription)
    TextView productDesc;
    @BindView(R.id.productPrice)
    TextView productPrice;
    @BindView(R.id.productTags)
    TextView productTags;
    @BindView(R.id.storeName)
    TextView storeName;
    @BindView(R.id.shareProgress)
    ProgressBar progressBar;
    private LatLng coord;
    private ImageLoader imageLoader;
    private Bundle bundle;
    private ProductViewActivity productView;
    private String data;
    private ProgressDialog progressDialog;
    private NativeAd nativeAd;

    @OnClick(R.id.vistStore)
    void visit() {
        Intent i = new Intent(ProductViewActivity.this, StoreActivity.class);
        StoreModel storeModel = new StoreModel();
        storeModel.setSlug(productModel.OwnerSlug);
        storeModel.setName(productModel.getOwner());
        storeModel.setLogo(productModel.getOwnerLogo());
        i.putExtra("data", storeModel);
        startActivity(i);
    }

    @OnClick(R.id.map_layout)
    void GetDirecitons() {
        //StartGps();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
              Uri.parse("http://maps.google.com/maps?daddr=" + productModel.getCoordinates()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bundle = outState;
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
        ButterKnife.bind(this);
        fileCache = new FileCache(this);
        Intent intent = getIntent();
        if (intent.getDataString() != null) {
            data = intent.getDataString();
        }
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        imageLoader = VolleySingleton.getsInstance().getImageLoader();
        sharePhoto = new SharePhoto.Builder();
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData();
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
        progressBar.setVisibility(View.GONE);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public String downloadImage(String Imageurl) throws MalformedURLException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppEventsLogger.logItemDownloadEvent(productModel.getName(), productModel.getShop(), productModel.getCategory());
                Toast.makeText(ProductViewActivity.this, "Saving image.......", Toast.LENGTH_LONG).show();
            }
        });
        String type = "jpg";
        final File file = fileCache.getFile(productModel.getName(), type);
        final URL url = new URL(Imageurl);
        Thread tm = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                            Toast.makeText(ProductViewActivity.this, "Product Image saved to shop440" + " " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        tm.start();
        Log.d(TAG, file.getAbsolutePath());
        return "ok";
    }

    private void initUi() {
        Typeface robotMedium = Typeface.createFromAsset(getAssets(),
              "fonts/Roboto-Medium.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(getAssets(),
              "fonts/RobotoCondensed-Regular.ttf");
        Typeface robotBold = Typeface.createFromAsset(getAssets(),
              "fonts/RobotoCondensed-Bold.ttf");
        Typeface robotThinItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        if (!productModel.getCoordinates().isEmpty()) {
            latlng = productModel.getCoordinates().split(",");
            coord = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]));
        }
        productPrice.setTypeface(robotMedium);
        productName.setTypeface(robotMedium);
        productDesc.setTypeface(robotThinItalic);
        productName.setText(productModel.getName());
        productDesc.setText(productModel.getDescription());
        productPrice.setText(productModel.getPrice());
        storeName.setText(productModel.getOwner());
        byte[] imageByte = Base64.decode(productModel.getPlaceholder(), Base64.DEFAULT);
        final Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        imageView.setImageBitmap(bit);
        content = new ShareLinkContent.Builder()
              .setContentUrl(Uri.parse("https://shop440.com/products/" + productModel.getSlug()))
              .setContentTitle(productModel.getName())
              .setImageUrl(Uri.parse(productModel.getImage()))
              .build();
       // SharePhoto.Builder sharePhoto = new SharePhoto.Builder();
        sharePhoto.setImageUrl(Uri.parse(productModel.getImage()));
        final ShareDialog shareDialog = new ShareDialog(this);
        CardView shareCard = (CardView) findViewById(R.id.shareCard);
       // ShareContent shareContent = new ShareMediaContent.Builder().addMedium(sharePhoto.build()).build();
        shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger.logItemShareEvent();
                progressBar.setVisibility(View.VISIBLE);
                /*Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("https://shop440.com/product/" + productModel.getSlug()).toString());
                startActivity(Intent.createChooser(share, productModel.getName()));*/
                shareDialog.show(content);
            }
        });
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
        showNativeAd();
        progressDialog.show();

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
    public void onMapReady(GoogleMap googleMap) {
        Log.d("here", "here");
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(coord);
        googleMap.addMarker(new MarkerOptions().position(coord).title(productModel.getShop()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
        map.onResume();
        progressDialog.dismiss();
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

        if (id == R.id.download) {
            try {
                downloadImage(productModel.getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        if (data != null) {
            String resolvedUrl = data.substring(data.lastIndexOf("/") + 1);
            Log.i("URI", resolvedUrl);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.loader_text));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            requestQueue.add(new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.GETPRODUCT + resolvedUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    try {
                        productModel = new ProductModel();
                        productModel.setName(object.getString("Name"));
                        productModel.setDescription(object.getString("Description"));
                        productModel.setPrice(object.getString("Price"));
                        productModel.setCategory(object.getString("Category"));
                        productModel.setCity(object.getString("City"));
                        productModel.setSlug(object.getString("Slug"));
                        productModel.setCitySlug(object.getString("CitySlug"));
                        productModel.setOwner(object.getJSONObject("Store").getString("Name"));
                        productModel.setOwnerSlug(object.getJSONObject("Store").getString("Slug"));
                        productModel.setOwnerLogo(object.getJSONObject("Store").getString("Logo"));
                        productModel.setSpecialisation(object.getJSONObject("Store").getString("Specialisation"));
                        productModel.setImage(object.getJSONObject("Image").getString("Path"));
                        String[] placeholder = object.getJSONObject("Image").getString("Placeholder").split("data:image/jpeg;base64,");
                        try {
                            productModel.setPlaceholder(placeholder[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            productModel.setPlaceholder(" ");

                        }
                        try {
                            productModel.setCoordinates(object.getJSONObject("Location").getJSONArray("Coordinates").getString(0) + "," + object.getJSONObject("Location").getJSONArray("Coordinates").getString(1));
                        } catch (ArrayIndexOutOfBoundsException a) {
                            a.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initUi();
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }).setRetryPolicy(new DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));

        } else {
            productModel = (ProductModel) getIntent().getSerializableExtra("data");
            initUi();
        }
    }

    private void showNativeAd() {
        nativeAd = new NativeAd(this, "909211035848244_1018000754969271");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }

                // Add the Ad view into the ad container.
                LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(ProductViewActivity.this);
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                View adView = inflater.inflate(R.layout.ad_layout, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdBody.setText(nativeAd.getAdBody());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(ProductViewActivity.this, nativeAd, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Request an ad
        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
    }
}
