package com.shop440.notification

import com.shop440.repository.api.Urls
import com.shop440.repository.api.response.GenericResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRequest{
    @GET(Urls.NOTIFICATIONS)
    fun getNotifications() : Call<List<NotificationModel>>

    @POST(Urls.NOTIFICATIONS)
    fun subscribe(@Body model: DeviceModel) : Call<GenericResponse>
}