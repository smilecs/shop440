package com.shop440.productview

import com.shop440.dao.models.ProductFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mmumene on 09/09/2017.
 */

interface ApiRequest {
    @GET("")
    fun getProduct(@Path("slug") slug: String): Call<ProductFeed>
}
