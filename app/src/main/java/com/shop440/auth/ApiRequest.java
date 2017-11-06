package com.shop440.auth;

import com.shop440.models.User;
import com.shop440.response.OtpResponse;
import com.shop440.response.UserResponse;
import com.shop440.api.Urls;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mmumene on 03/09/2017.
 */

public interface ApiRequest {
    @POST(Urls.NEW_USER)
    Call<UserResponse> createUser(@Body User user);

    @POST(Urls.LOGIN)
    Call<User> login(@Body User user);

    @GET(Urls.OTP)
    Call<JSONObject> requestOtp(@Query("p") String query);
}
