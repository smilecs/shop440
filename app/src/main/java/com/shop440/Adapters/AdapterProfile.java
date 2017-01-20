package com.shop440.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shop440.Models.Store;
import com.shop440.R;
import com.shop440.Utils.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by SMILECS on 1/15/17.
 */

public class AdapterProfile  extends RecyclerView.Adapter<AdapterProfile.ViewHolder>{
    ArrayList<Store> stores;
    Context c;
    public AdapterProfile(Context c, ArrayList<Store> stores){
        this.stores = stores;
        this.c = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.storeName);
            image = (ImageView) itemView.findViewById(R.id.store_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_store_list_card, parent, false);
        AdapterProfile.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.title.setText(store.getName());
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        imageLoader.get(store.getLogo(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.image.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
}
