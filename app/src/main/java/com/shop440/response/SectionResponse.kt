package com.shop440.response

import com.google.gson.annotations.SerializedName
import com.shop440.models.Feed

/**
 * Created by mmumene on 19/11/2017.
 */
object SectionResponse{
    data class SectionResponse(@SerializedName("type") val feedType:String,
                               @SerializedName("title") val title:String,
                               val content:String,
                               @SerializedName("product_feed") val productFeed:List<Feed.ProductFeed>,
                               @SerializedName("shop_feed") val shopFeed:List<Feed.StoreFeed>
    )
    data class HomeSection(val sections:List<SectionResponse>)
}