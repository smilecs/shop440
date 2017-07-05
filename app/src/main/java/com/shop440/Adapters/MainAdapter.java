package com.shop440.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.toolbox.ImageLoader;
import com.shop440.Models.ProductModel;
import com.shop440.ProductView;
import com.shop440.R;
import com.shop440.Utils.AppEventsLogger;
import com.shop440.Utils.Texts;
import com.shop440.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SMILECS on 1/21/17.
 */

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    ArrayList<ProductModel> model;
    Context c;
    ImageLoader imageLoader;

    public MainAdapter(Context c, ArrayList<ProductModel> model){
        this.model = model;
        this.c = c;
        imageLoader = VolleySingleton.getsInstance().getImageLoader();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ProductName, price, product;
        ImageView logo;
        ImageView productDisplay;

        public ViewHolder(final View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price);
            ProductName = (TextView) itemView.findViewById(R.id.storeName);
            //productDisplay = (ImageView) itemView.findViewById(R.id.mainImage);
            product = (TextView) itemView.findViewById(R.id.product);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel productModel = (ProductModel) itemView.getTag();
                    AppEventsLogger.logItemSelectedEvent(productModel.getName(), productModel.getShop());
                    Intent i = new Intent(itemView.getContext(), ProductView.class);
                    i.putExtra("data", productModel);
                    itemView.getContext().startActivity(i);
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered, parent, false);
        MainAdapter.ViewHolder vh = new ViewHolder(v);
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
        byte[] imageByte = Base64.decode(store.getPlaceholder(), Base64.CRLF);
        Bitmap bit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        holder.itemView.setTag(store);
        holder.ProductName.setTypeface(robotMedium);
        holder.product.setTypeface(robotCondensed);
        holder.ProductName.setText(store.getOwner().trim());
        holder.product.setText(store.getName().trim());
        holder.price.setTypeface(robotBold);
        holder.price.setText(c.getString(R.string.price_display, Texts.NIGERIA_CURRENCY_CODE, store.getPrice()));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
