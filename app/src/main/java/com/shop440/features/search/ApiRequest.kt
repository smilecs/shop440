package com.shop440.features.search

import com.shop440.repository.api.Urls
import com.shop440.repository.api.response.FilterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mmumene on 25/02/2018.
 */
interface ApiRequest {

    @GET(Urls.SEARCH)
    fun getFilterList(@Query("q") q: String,
                      @Query("p") p: String, @Query("cat") cat: String,
                      @Query("tag") tag: String): Call<FilterResponse>
}