package com.shop440.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shop440.Models.ProductModel;
import com.shop440.R;
import com.shop440.Utils.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by SMILECS on 1/24/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
    ArrayList<ProductModel> model;
    Context c;
    public StoreAdapter(Context c, ArrayList<ProductModel> model){
        this.model = model;
        this.c = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView StoreName, price, product;
        ImageView productDisplay, logo;
        public ViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            StoreName = (TextView) itemView.findViewById(R.id.storeName);
            productDisplay = (ImageView) itemView.findViewById(R.id.mainImage);
            product = (TextView) itemView.findViewById(R.id.product);
            logo = (ImageView) itemView.findViewById(R.id.logo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered, parent, false);
        StoreAdapter.ViewHolder vh = new StoreAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Typeface robotMedium = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Medium.ttf");
        Typeface robotThin = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Thin.ttf");
        Typeface robotCondensed = Typeface.createFromAsset(c.getAssets(),
                "fonts/RobotoCondensed-Light.ttf");
        Typeface robotBold = Typeface.createFromAsset(c.getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        ProductModel store = model.get(position);
        byte[] imageByte = Base64.decode(store.getPlaceholder(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        holder.productDisplay.setImageBitmap(bit);
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        imageLoader.get(store.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.productDisplay.setImageBitmap(response.getBitmap());
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        holder.StoreName.setTypeface(robotMedium);
        holder.StoreName.setTypeface(robotCondensed);
        holder.product.setTypeface(robotThin);
        holder.StoreName.setText(store.getOwner());
        holder.product.setText(store.getName());
        holder.price.setTypeface(robotBold);
        holder.price.setText(store.getPrice());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
