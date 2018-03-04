package com.shop440.search

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.shop440.R
import com.shop440.dao.models.CategoryModel
import com.shop440.dao.models.Product
import com.shop440.utils.Image
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.home_feed_product_layout.view.*

/**
 * Created by mmumene on 25/02/2018.
 */

class SearchResultAdapter(val requestManager: RequestManager, val model: MutableList<Product>) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productTitle = view.productFeedTitle
        val productPrice = view.productFeedPrice!!
        val productShopTitle = view.productShopTitle
        val preview = view.productPreview

        init {
            view.setOnClickListener { view ->
                val toPass = itemView.tag as CategoryModel
                val i = Intent(view.context, SearchResultFragment::class.java)
                i.putExtra("query", toPass.slug)
                i.putExtra("title", toPass.catName)
                i.putExtra("isSearch", false)
                view.context.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_feed_product_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchResultAdapter.ViewHolder, position: Int) {
        val product = model[position]
        holder.productPrice.text = Metrics.getDisplayPriceWithCurrency(holder.itemView.context, product.productPrice)
        holder.productTitle.text = product.productName
        holder.productShopTitle.text = product.shop.title
        product.images?.get(0)?.let {
            val requestListener = object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    resource?.let {
                        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)

                        val canvas = Canvas(bitmap)
                        it.setBounds(0, 0, canvas.width, canvas.height)
                        it.draw(canvas)
                        Palette.Builder(bitmap).generate { result ->
                            holder.productTitle.setTextColor(result.getLightVibrantColor(holder.itemView.resources.getColor(R.color.sub_header_text)))
                        }
                    }
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }
            requestManager.load("https://tinyfiles.past3dev.com/resize?width=600&height=0&type=jpeg&nocrop=true&url=" + it.url)
                    ?.apply(RequestOptions().placeholder(BitmapDrawable(holder.itemView.resources, Image.base64ToBitmap(it.placeholder!!))))
                    ?.listener(requestListener)
                    ?.into(holder.preview)
        }

    }

    override fun getItemCount(): Int {
        return model.size
    }
}
