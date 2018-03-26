package com.shop440.features.checkout.models

import com.shop440.R
import com.shop440.navigation.home.adaptermodel.AdapterModel

/**
 * Created by mmumene on 10/02/2018.
 */
class SummaryAdapterModel(title:String, val item: List<ItemForKart>) : AdapterModel(title, "") {
    override fun type() = R.layout.summary_item_layout

    override fun size() = item.size
}