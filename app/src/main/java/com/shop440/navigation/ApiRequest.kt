package com.shop440.navigation

import com.shop440.api.Urls
import com.shop440.dao.models.CategoryModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mmumene on 31/12/2017.
 */
interface ApiRequest{
    @GET(Urls.CATEGORY)
    fun getCategories():Call<List<CategoryModel>>
}