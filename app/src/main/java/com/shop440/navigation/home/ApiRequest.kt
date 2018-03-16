package com.shop440.navigation.home

import com.shop440.api.Urls
import com.shop440.resp.HomeSection
import com.shop440.resp.SectionResponse

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by mmumene on 09/09/2017.
 */

interface ApiRequest {
    @GET(Urls.HOME_PAGE_SECTION)
    fun homeSection():Call<HomeSection>

    /*@GET(Urls.GETPRODUCTS)
    fun getProductFeeds(@Query("p") page: String): Call<ProductModel>*/

}
