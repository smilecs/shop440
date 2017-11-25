package com.shop440.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shop440.models.ProductModel;
import com.shop440.R;

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
    }

    @Override
    public int getItemCount() {
        Log.d("GalleryAdapter", Integer.toString(model.size()));
        return model.size();
    }
}
