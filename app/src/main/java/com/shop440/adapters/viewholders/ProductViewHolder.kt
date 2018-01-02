package com.shop440.adapters.viewholders

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.shop440.R
import com.shop440.adapters.viewmodel.ProductViewModel
import com.shop440.productview.ProductViewActivity
import com.shop440.utils.Image
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.home_feed_product_layout.view.*
import java.net.URL

/**
 * Created by mmumene on 21/11/2017.
 */

class ProductViewHolder(val view: View):BaseViewHolder<ProductViewModel>(view){
    private val productTitle = view.productFeedTitle
    private val productPrice = view.productFeedPrice
    private val productShopTitle = view.productShopTitle
    private val preview = view.productPreview



    override fun bind(item: ProductViewModel, position:Int) {
        val product = item.viewModel[position]
        //productShopTitle.text = product.productDesc
        productPrice.text = Metrics.getDisplayPriceWithCurrency(view.context, product.productPrice)
        productTitle.text = product.productName
        productShopTitle.text = product.shop.title
        product.images?.get(0)?.let {
            it.placeholder?.let {
                //preview.setImageBitmap(Image.base64ToBitmap(it))
            }

            val url = Uri.Builder().path("https://tinyfiles.past3dev.com/resize")
            url.appendQueryParameter("width", "200")
            url.appendQueryParameter("height", "0")
            url.appendQueryParameter("type", "jpeg")
            url.appendQueryParameter("nocrop", "true")
            url.appendQueryParameter("url", Uri.parse(it.url).toString())
            val url2 = url.build()
            Glide.with(view)
                    .load("https://tinyfiles.past3dev.com/resize?width=300&height=0&type=jpeg&nocrop=true&url="+it.url)
                    .apply(RequestOptions().placeholder(BitmapDrawable(view.resources, Image.base64ToBitmap(it.placeholder!!))))
                    .into(preview)
        }

        view.setOnClickListener {
            val intent = Intent(it.context, ProductViewActivity::class.java)
            intent.putExtra("data", product)
            it.context.startActivity(intent)
        }

    }
}