package com.shop440.checkout.models

import com.shop440.R
import com.shop440.navigation.home.viewmodel.ViewModel

/**
 * Created by mmumene on 10/02/2018.
 */
class SummaryViewModel(title:String, val item: List<ItemForKart>) : ViewModel(title) {
    override fun type() = R.layout.summary_item_layout

    override fun size() = item.size
}