package com.shop440.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shop440.models.CategoryModel;
import com.shop440.R;
import com.shop440.SearchResultActivity;

import java.util.ArrayList;

/**
 * Created by SMILECS on 2/7/17.
 */

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.ViewHolder>{
    ArrayList<CategoryModel> model;
    Context c;

    public CategorySearchAdapter(Context c, ArrayList<CategoryModel> model){
        this.c = c;
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
                    CategoryModel toPass = (CategoryModel) itemView.getTag();
                    Intent i = new Intent(view.getContext(), SearchResultActivity.class);
                    i.putExtra("query", toPass.getSlug());
                    i.putExtra("title", toPass.getName());
                    i.putExtra("isSearch", false);
                    view.getContext().startActivity(i);
                }
            });
        }
    }

    @Override
    public CategorySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        CategorySearchAdapter.ViewHolder vh = new CategorySearchAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategorySearchAdapter.ViewHolder holder, int position) {
        Typeface robotCondensed = Typeface.createFromAsset(c.getAssets(),
                "fonts/Roboto-Thin.ttf");
        CategoryModel categoryModel = model.get(position);
        holder.category.setTypeface(robotCondensed);
        holder.category.setText(categoryModel.getName());
        holder.itemView.setTag(categoryModel);
        //holder.category.setTag(md);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
