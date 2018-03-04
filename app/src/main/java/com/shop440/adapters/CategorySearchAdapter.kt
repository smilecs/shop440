package com.shop440.adapters


import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shop440.R
import com.shop440.dao.models.CategoryModel
import java.util.*

/**
 * Created by SMILECS on 2/7/17.
 */

class CategorySearchAdapter(val c: Context, val model: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategorySearchAdapter.ViewHolder>() {
    lateinit var categoryQuery:CategoryQuery
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var category: TextView = itemView.findViewById(R.id.category) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorySearchAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.category_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategorySearchAdapter.ViewHolder, position: Int) {
        val robotCondensed = Typeface.createFromAsset(c.assets,
                "fonts/Roboto-Thin.ttf")
        val categoryModel = model[position]
        holder.category.typeface = robotCondensed
        holder.category.text = categoryModel.catName
        holder.itemView.tag = categoryModel
        holder.itemView.setOnClickListener { _ ->
            categoryQuery.onCategorySelected(categoryModel.slug)
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    interface CategoryQuery{
        fun onCategorySelected(cat:String)
    }
}