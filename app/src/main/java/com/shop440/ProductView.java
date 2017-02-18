package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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


public class ProductView extends AppCompatActivity {
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
    //StaggeredGridLayoutManager layoutManager;
    Boolean next = true;
    @BindView(R.id.shareText) TextView shareText;
    @BindView(R.id.productImage) NetworkImageView imageView;
    @BindView(R.id.productName) TextView productName;
    @BindView(R.id.productDescription) TextView productDesc;
    @BindView(R.id.productPrice) TextView productPrice;
    @BindView(R.id.productTags) TextView productTags;
    //@BindView(R.id.recyclerView) RecyclerView list;
    @BindView(R.id.storeName) TextView storeName;
    @BindView(R.id.shareProgress) ProgressBar progressBar;
    @OnClick(R.id.download) void Download(){
        try{
            downloadImage(productModel.getImage());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*@OnClick(R.id.shareCard) void shareItem(){
        Log.d(TAG, "facebookClick");
        //shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);

        ShareDialog.show(ProductView.this, content);

    }*/

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;
        ButterKnife.bind(this);
        fileCache = new FileCache(this);
        productModel = (ProductModel) getIntent().getSerializableExtra("data");
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        Typeface robotMedium = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        Typeface robotBold = Typeface.createFromAsset(getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        Typeface robotThinItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-ThinItalic.ttf");
        productPrice.setTypeface(robotBold);
        productName.setTypeface(robotMedium);
        productDesc.setTypeface(robotThinItalic);
        productName.setText(productModel.getName());
        productDesc.setText(productModel.getDescription());
        productPrice.setText(productModel.getPrice());
        storeName.setText(productModel.getOwner());
        byte[] imageByte = Base64.decode(productModel.getPlaceholder(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        imageView.setImageBitmap(bit);
        imageView.setImageUrl(productModel.getImage(), imageLoader);
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
        /*if (ShareDialog.canShow(ShareLinkContent.class)) {
         */
            content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://shop440.com/api/products/"+productModel.getSlug()))
                    .setContentTitle(productModel.getName())
                    .setImageUrl(Uri.parse(productModel.getImage()))
                    .build();
            /*SharePhoto sharePhoto = new SharePhoto.Builder().setImageUrl(Uri.parse(productModel.getImage())).setCaption(productModel.getName()).build();
            final ShareContent shareContent = new ShareMediaContent.Builder()
                    .addMedium(sharePhoto).setContentUrl(Uri.parse("https://shop440.com/api/products/"+productModel.getSlug())).build();*/
            final ShareDialog shareDialog = new ShareDialog(this);



            //shareDialog.show(content);
        //}
        CardView shareCard = (CardView) findViewById(R.id.shareCard);
        shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }
        });


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

}
