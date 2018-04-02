package com.shop440.typefactory

import android.view.View
import com.bumptech.glide.RequestManager
import com.shop440.R
import com.shop440.features.checkout.viewholder.SummaryViewHolder
import com.shop440.BaseViewHolder
import com.shop440.features.navigation.home.viewholders.ProductViewHolder
import com.shop440.features.navigation.home.viewholders.StoreViewHolder

/**
 * Created by mmumene on 21/11/2017.
 */

class TypesFactoryImpl : TypeFactory {

    override fun holder(type: Int, view: View, requestManager: RequestManager?): BaseViewHolder<*> =
            when (type) {
                R.layout.home_feed_product_layout -> ProductViewHolder(view, requestManager)
                R.layout.home_feed_shop_layout -> StoreViewHolder(view)
                R.layout.summary_item_layout -> SummaryViewHolder(view)
                else -> throw RuntimeException("Illegal view type")
            }
}