package com.shop440.productview;

import com.shop440.api.Urls;
import com.shop440.models.Datum;

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
