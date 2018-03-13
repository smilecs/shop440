package com.shop440.productview

import com.shop440.api.Urls
import com.shop440.dao.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mmumene on 09/09/2017.
 */

interface ApiRequest {
    @GET(Urls.GET_PRODUCT)
    fun getProduct(@Path("shopid") shop:String, @Path("productslug") slug: String): Call<Product>
}
