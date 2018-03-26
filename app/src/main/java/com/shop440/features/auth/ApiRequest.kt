package com.shop440.features.auth

import com.shop440.repository.dao.models.User
import com.shop440.repository.api.response.OtpResponse
import com.shop440.repository.api.response.UserResponse
import com.shop440.repository.api.Urls

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by mmumene on 03/09/2017.
 */

interface ApiRequest {
    @POST(Urls.NEW_USER)
    fun createUser(@Body user: User): Call<UserResponse>

    @POST(Urls.LOGIN)
    fun login(@Body user: User): Call<UserResponse>

    @GET(Urls.OTP)
    fun requestOtp(@Query("p") query: String): Call<OtpResponse>

    @POST(Urls.CHECKPHONE)
    fun checkAvailability(@Body user: User) : Call<User>
}
