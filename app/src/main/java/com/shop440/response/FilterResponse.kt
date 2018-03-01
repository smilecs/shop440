package com.shop440.response

import com.google.gson.annotations.SerializedName
import com.shop440.dao.models.Page
import com.shop440.dao.models.Product

/**
 * Created by mmumene on 25/02/2018.
 */

data class FilterResponse(@SerializedName("data") val data: List<Product>,
                          val page: Page,
                          val query: String)