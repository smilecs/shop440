package com.shop440.Adapters.ViewHolders

import android.view.View
import com.shop440.Adapters.ViewModel.TypeFactory
import com.shop440.R
import com.shop440.models.Feed

/**
 * Created by mmumene on 21/11/2017.
 */

class TypesFactoryImpl : TypeFactory {

    override fun holder(type: Int, view: View): FeedViewHolder<*> =
            when (type) {
                R.layout.home_feed_product_layout -> ProductViewHolder(view)
                R.layout.home_feed_shop_layout -> StoreViewHolder(view)
                else -> throw RuntimeException("Illegal view type")
            }
}