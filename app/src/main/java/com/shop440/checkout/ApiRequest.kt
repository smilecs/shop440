package com.shop440.checkout

import com.shop440.api.Urls
import com.shop440.api.request.OrderRequest
import com.shop440.checkout.models.Order
import com.shop440.api.response.GenericResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by mmumene on 22/02/2018.
 */
interface ApiRequest {
    @POST(Urls.NEW_ORDER)
    fun newOrder(@Body order:OrderRequest) : Call<GenericResponse>

    @GET(Urls.GET_ORDER)
    fun getOrders(@Path("userPhone") phone:String) : Call<Order>
}