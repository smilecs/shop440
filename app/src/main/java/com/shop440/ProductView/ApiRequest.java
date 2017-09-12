package com.shop440.ProductView;

import com.shop440.Api.Urls;
import com.shop440.Models.Datum;
import com.shop440.Models.ProductModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mmumene on 09/09/2017.
 */

public interface ApiRequest {
    @GET(Urls.GETPRODUCTS)
    Call<Datum> getProduct(@Path("slug") String slug);
}
