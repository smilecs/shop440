package com.shop440.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView StoreName, price;
        ImageView profileLogo, ProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            StoreName = (TextView) itemView.findViewById(R.id.storeName);
            profileLogo = (ImageView) itemView.findViewById(R.id.mainImage);
            ProfileImage = (ImageView) itemView.findViewById(R.id.logo);
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
        holder.StoreName.setText(store.getName());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
