package com.shop440.resp

import com.google.gson.annotations.SerializedName
import com.shop440.dao.models.Product
import com.shop440.dao.models.StoreFeed

/**
 * Created by mmumene on 19/11/2017.
 */
 data class SectionResponse(@SerializedName("type") val feedType:String,
                               @SerializedName("title") val title:String,
                               val content:String,
                               @SerializedName("product_feed") val product:MutableList<Product>,
                               @SerializedName("shop_feed") val shopFeed:MutableList<StoreFeed>
    )