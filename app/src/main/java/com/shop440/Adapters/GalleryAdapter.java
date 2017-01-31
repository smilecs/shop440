package com.shop440.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.shop440.Models.ProductModel;
import com.shop440.R;
import com.shop440.Utils.VolleySingleton;

import java.util.ArrayList;


/**
 * Created by SMILECS on 9/10/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    ArrayList<ProductModel> model;

    public GalleryAdapter(ArrayList<ProductModel> model){
        this.model = model;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.gallery);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductModel mode = model.get(position);
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        byte[] imageByte = Base64.decode(mode.getImage(), Base64.DEFAULT);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        holder.image.setImageBitmap(bit);
        //holder.image.

    }

    @Override
    public int getItemCount() {
        Log.d("GalleryAdapter", Integer.toString(model.size()));
        return model.size();
    }
}