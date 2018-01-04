package com.shop440.productview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.shop440.models.Datum

/**
 * Created by SMILECS on 1/24/17.
 */

class ProductAdapter(internal var c: Context, internal var model: List<Datum>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var StoreName: TextView? = null
        internal var price: TextView? = null
        internal var product: TextView? = null
        internal var category: TextView? = null
        internal var location: TextView? = null
        internal var logo: ImageView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return model.size
    }
}
