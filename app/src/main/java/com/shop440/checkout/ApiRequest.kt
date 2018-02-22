package com.shop440.checkout

import com.shop440.api.Urls
import com.shop440.checkout.models.Order
import com.shop440.response.GenericResponse
import retrofit2.Call
import retrofit2.http.POST

/**
 * Created by mmumene on 22/02/2018.
 */
interface ApiRequest {
    @POST(Urls.NEW_ORDER)
    fun newOrder(order:Order) : Call<GenericResponse>
}