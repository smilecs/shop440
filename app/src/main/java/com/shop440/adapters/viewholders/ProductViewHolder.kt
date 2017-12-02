package com.shop440.adapters.viewholders

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.shop440.adapters.viewmodel.ProductViewModel
import com.shop440.productview.ProductViewActivity
import com.shop440.utils.Image
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.home_feed_product_layout.view.*

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
        product.images?.get(0)?.let {
            it.placeholder?.let {
                preview.setImageBitmap(Image.base64ToBitmap(it))
            }
            Glide.with(view)
                    .load(it.url)
                    .into(preview)

        }

        view.setOnClickListener {
            val intent = Intent(it.context, ProductViewActivity::class.java)
            intent.putExtra("data", product)
            it.context.startActivity(intent)
        }

    }
}