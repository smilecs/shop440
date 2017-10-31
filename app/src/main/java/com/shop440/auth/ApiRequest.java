package com.shop440.auth;

import com.shop440.Models.User;
import com.shop440.Api.Urls;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mmumene on 03/09/2017.
 */

public interface ApiRequest {
    @POST(Urls.NEW_USER)
    Call<User> createUser(@Body User user);

    @POST(Urls.LOGIN)
    Call<User> login(@Body User user);
}
