package com.shop440.productview.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shop440.R
import com.shop440.models.Image


/**
 * Created by SMILECS on 9/10/16.
 */
class GalleryAdapter(var model: List<Image>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.imageGalleryPreview)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_gallery, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = model[position]
            Glide.with(holder.itemView)
                    .load(image.url)
                    .into(holder.image)

        }

    override fun getItemCount(): Int = model.size


}

