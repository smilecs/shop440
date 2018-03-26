package com.shop440.navigation

import com.shop440.repository.api.Urls
import com.shop440.repository.dao.models.CategoryModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mmumene on 31/12/2017.
 */
interface ApiRequest{
    @GET(Urls.CATEGORY)
    fun getCategories():Call<List<CategoryModel>>
}