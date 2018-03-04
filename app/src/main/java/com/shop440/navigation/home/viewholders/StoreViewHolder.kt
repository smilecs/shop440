package com.shop440.navigation.home.viewholders

import android.view.View
import com.shop440.BaseViewHolder
import com.shop440.navigation.home.adaptermodel.StoreAdapterModel
import com.shop440.utils.Image
import kotlinx.android.synthetic.main.home_feed_shop_layout.view.*

/**
 * Created by mmumene on 21/11/2017.
 */


class StoreViewHolder(view:View): BaseViewHolder<StoreAdapterModel>(view){
    val shopPreview = view.shopPreview
    val shopTitle = view.shopFeedTitle
    val shopAddress = view.shopFeedAddress
    override fun bind(item: StoreAdapterModel, position:Int) {
        val feed = item.viewModel[position]
        shopAddress.text = feed.address
        shopTitle.text = feed.shopName
        shopPreview.setImageBitmap(Image.base64ToBitmap(feed.logo))
    }
}