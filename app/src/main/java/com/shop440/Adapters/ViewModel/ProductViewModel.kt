package com.shop440.Adapters.ViewModel

import com.shop440.R
import com.shop440.models.Feed
import com.shop440.models.ProductFeed

/**
 * Created by mmumene on 21/11/2017.
 */

class ProductViewModel(viewTitle: String, val viewModel: List<ProductFeed>) : ViewModel(viewTitle) {

    override fun size() = viewModel.size

    override fun type() = R.layout.home_feed_product_layout

}