package com.shop440.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mmumene on 19/11/2017.
 */
data class SectionResponse(@SerializedName("type") val feedType:String,
                           @SerializedName("title") val title:String,
                           )