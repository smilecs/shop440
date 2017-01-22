package com.shop440.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shop440.Models.Store;
import com.shop440.R;

import java.util.ArrayList;

/**
 * Created by SMILECS on 1/21/17.
 */

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    ArrayList<Store> model;
    Context c;

    public MainAdapter(Context c, ArrayList<Store> model){
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
        MainAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = model.get(position);
        try{
            byte[] imageByte = Base64.decode(store.getPlaceholder(), Base64.DEFAULT);
            Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            holder.productDisplay.setImageBitmap(bit);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.StoreName.setText(store.getOwner());
        holder.product.setText(store.getName());
        Glide.with(c).load(store.getImage()).into(holder.productDisplay);

        holder.price.setText(store.getPrice());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
