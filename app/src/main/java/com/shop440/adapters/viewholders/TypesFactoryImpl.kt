package com.shop440.adapters.viewholders

import android.view.View
import com.shop440.adapters.viewmodel.TypeFactory
import com.shop440.R

/**
 * Created by mmumene on 21/11/2017.
 */

class TypesFactoryImpl : TypeFactory {

    override fun holder(type: Int, view: View): BaseViewHolder<*> =
            when (type) {
                R.layout.home_feed_product_layout -> ProductViewHolder(view)
                R.layout.home_feed_shop_layout -> StoreViewHolder(view)
                else -> throw RuntimeException("Illegal view type")
            }
}