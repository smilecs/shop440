package com.shop440.response

import com.google.gson.annotations.SerializedName
import com.shop440.models.ProductFeed
import com.shop440.models.StoreFeed

/**
 * Created by mmumene on 19/11/2017.
 */
object SectionResponse{
    data class SectionResponse(@SerializedName("type") val feedType:String,
                               @SerializedName("title") val title:String,
                               val content:String,
                               @SerializedName("product_feed") val productFeed:List<ProductFeed>,
                               @SerializedName("shop_feed") val shopFeed:List<StoreFeed>
    )
    data class HomeSection(val sections:List<SectionResponse>)
}