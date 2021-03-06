package com.shop440.features.navigation.home.adaptermodel

import com.shop440.R
import com.shop440.repository.dao.models.Product

/**
 * Created by mmumene on 21/11/2017.
 */

class ProductModel(viewTitle: String, val viewModel: MutableList<Product>) : AdapterModel(viewTitle, viewModel[0].category) {
    override fun size() = viewModel.size

    override fun type() = R.layout.home_feed_product_layout

}