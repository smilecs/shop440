package com.shop440.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shop440.Models.CategoryModel;
import com.shop440.Models.StoreModel;
import com.shop440.NewProduct;
import com.shop440.R;

import java.util.ArrayList;

/**
 * Created by SMILECS on 1/24/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    ArrayList<CategoryModel> model;
    final StoreModel md;

    public CategoryAdapter(Context c, ArrayList<CategoryModel> model, StoreModel md){
        //this.c = c;
        this.md = md;
        this.model = model;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        public ViewHolder(final View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), NewProduct.class);
                    i.putExtra("data", (CategoryModel) itemView.getTag());
                    i.putExtra("backtrack", (StoreModel) category.getTag());
                    view.getContext().startActivity(i);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        CategoryAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryModel categoryModel = model.get(position);
        holder.category.setText(categoryModel.getName());
        holder.itemView.setTag(categoryModel);
        holder.category.setTag(md);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
