package com.shop440.adapters.viewmodel

import com.shop440.R
import com.shop440.models.StoreFeed

/**
 * Created by mmumene on 21/11/2017.
 */

class StoreViewModel(viewTitle:String, val viewModel: List<StoreFeed>): ViewModel(viewTitle){

    override fun type() = R.layout.home_feed_shop_layout
    override fun size() = viewModel.size
}