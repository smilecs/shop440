package com.shop440.repository.dao.models

import com.google.gson.annotations.SerializedName

/**
 * Created by mmumene on 21/11/2017.
 */
data class StoreFeed(@SerializedName("shop_id") val slug: String,
                     val shopName: String,
                     val address: String,
                     val logo: String,
                     val city: String,
                     val phone: String,
                     val description: String)