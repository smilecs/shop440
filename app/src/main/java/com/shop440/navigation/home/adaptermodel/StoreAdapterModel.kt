package com.shop440.navigation.home.adaptermodel

import com.shop440.R
import com.shop440.repository.dao.models.StoreFeed

/**
 * Created by mmumene on 21/11/2017.
 */

class StoreAdapterModel(viewTitle:String, val viewModel: List<StoreFeed>): AdapterModel(viewTitle, viewModel[0].slug){

    override fun type() = R.layout.home_feed_shop_layout
    override fun size() = viewModel.size
}