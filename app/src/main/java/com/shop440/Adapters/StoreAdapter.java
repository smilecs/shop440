package com.shop440.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shop440.Models.StoreModel;
import com.shop440.MyStoreActivity;
import com.shop440.R;
import com.shop440.StoreActivity;
import com.shop440.Utils.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by SMILECS on 1/15/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private ArrayList<StoreModel> stores;

    public StoreAdapter(ArrayList<StoreModel> stores) {
        this.stores = stores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_store_list_card, parent, false);
        StoreAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StoreModel store = stores.get(position);
        holder.title.setText(store.getName());
        holder.description.setText(store.getDescription());
        holder.itemView.setTag(store);
        ImageLoader imageLoader = VolleySingleton.getsInstance().getImageLoader();
        imageLoader.get(store.getLogo(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap() != null){
                    holder.image.setImageBitmap(response.getBitmap());
                }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;
        Context context;

        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.storeName);
            image = (ImageView) itemView.findViewById(R.id.store_image);
            description = (TextView) itemView.findViewById(R.id.profile_store_description_card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MyStoreActivity.class);
                    i.putExtra("data", (StoreModel) itemView.getTag());
                    context.startActivity(i);
                }
            });
        }
    }
}
