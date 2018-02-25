package com.shop440.navigation.home.viewholders

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.shop440.R
import com.shop440.navigation.home.viewmodel.ProductViewModel
import com.shop440.productview.ProductViewActivity
import com.shop440.utils.Image
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.home_feed_product_layout.view.*


/**
 * Created by mmumene on 21/11/2017.
 */

class ProductViewHolder(val view: View, val requestManager: RequestManager?) : BaseViewHolder<ProductViewModel>(view) {
    private val productTitle = view.productFeedTitle
    private val productPrice = view.productFeedPrice
    private val productShopTitle = view.productShopTitle
    private val preview = view.productPreview


    override fun bind(item: ProductViewModel, position: Int) {
        val product = item.viewModel[position]
        //productShopTitle.text = product.productDesc
        productPrice.text = Metrics.getDisplayPriceWithCurrency(view.context, product.productPrice)
        productTitle.text = product.productName
        productShopTitle.text = product.shop.title
        product.images?.get(0)?.let {
            val requestListener = object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    resource?.let {
                        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)

                        val canvas = Canvas(bitmap)
                        it.setBounds(0, 0, canvas.width, canvas.height)
                        it.draw(canvas)
                        Palette.Builder(bitmap).generate { result ->
                            productTitle.setTextColor(result.getLightVibrantColor(view.resources.getColor(R.color.sub_header_text)))
                        }
                    }
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }
            requestManager?.load("https://tinyfiles.past3dev.com/resize?width=600&height=0&type=jpeg&nocrop=true&url=" + it.url)
                    ?.apply(RequestOptions().placeholder(BitmapDrawable(view.resources, Image.base64ToBitmap(it.placeholder!!))))
                    ?.listener(requestListener)
                    ?.into(preview)
        }

        view.setOnClickListener {
            val intent = Intent(it.context, ProductViewActivity::class.java)
            intent.putExtra("data", product)
            it.context.startActivity(intent)
        }

    }
}