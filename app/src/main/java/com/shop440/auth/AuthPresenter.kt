package com.shop440.auth

import com.shop440.R
import com.shop440.dao.models.User
import com.shop440.api.response.OtpResponse
import com.shop440.api.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 25/08/2017.
 */

class AuthPresenter(val authView: AuthContract.View, val retrofit: Retrofit) : AuthContract.Presenter {

    init {
        authView.presenter = this
    }

    override fun start() {
    }

    override fun login(user: User) {
        authView.onDataLoading()
        val posts: Call<UserResponse> = retrofit.create(ApiRequest::class.java).login(user)
        posts.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                authView.onDataLoading()
                if (response?.isSuccessful!!) {
                    val resp = response.body()
                    resp?.let {
                        it.user.token = response.body()?.token!!
                        authView.saveUser(it.user)
                        return
                    }
                    authView.onError(R.string.signup_request_error)
                }
            }

            override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
                authView.onError(R.string.signup_request_error)
                authView.onDataLoading()
                t?.printStackTrace()
            }
        })
    }

    override fun signUp(user: User) {
        val posts: Call<UserResponse> = retrofit.create(ApiRequest::class.java).createUser(user)
        authView.onDataLoading()
        posts.enqueue(object : Callback<UserResponse?> {
            override fun onFailure(call: Call<UserResponse?>, t: Throwable?) {
                authView.onError(R.string.signup_request_error)
                authView.onDataLoading()
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<UserResponse?>, response: Response<UserResponse?>) {
                authView.onDataLoading()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val userData = response.body()?.user!!
                        userData.token = response.body()?.token!!
                        authView.saveUser(userData)
                        return
                    }
                    authView.onError(R.string.signup_request_error)
                }
            }
        })
    }

    override fun checkExisting(user: User) {
        val existing = retrofit.create(ApiRequest::class.java).checkAvailability(user)
        authView.onDataLoading()
        existing.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                authView.onDataLoading()
                if (response?.isSuccessful!!) {
                    authView.saveUser(user)
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                authView.onDataLoading()
                authView.onError(R.string.number_exist_error)
            }
        })
    }

    override fun onAuthComplete(isNewUser: Boolean) {

    }

    override fun onRequestOtp(phone: String, otpListener: AuthContract.OtpListener) {
        authView.onDataLoading()
        val otp = retrofit.create(ApiRequest::class.java).requestOtp(phone)
        otp.enqueue(object : Callback<OtpResponse?> {
            override fun onResponse(call: Call<OtpResponse?>, response: Response<OtpResponse?>) {
                if (response.isSuccessful) {
                    authView.onDataLoading()
                    otpListener.onOtpReceived(response.body()?.code)
                }
            }

            override fun onFailure(call: Call<OtpResponse?>, t: Throwable) {
                authView.onDataLoading()
                authView.onError(R.string.error_results)
            }
        })
    }
}