package com.shop440.Login

import com.shop440.Models.User
import com.shop440.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 25/08/2017.
 */

class LoginPresenter(val loginView:LoginContract.View, val retrofit: Retrofit):LoginContract.Presenter{

    init {
        loginView.presenter = this
    }
    override fun start() {
    }

    override fun login(user: User) {
        val posts: Call<User> = retrofit.create(ApiRequest::class.java).login(user)
        posts.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        loginView.saveUser(response.body()!!)
                        return
                    }
                    loginView.showFeedBack(R.string.signup_request_error)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                loginView.showFeedBack(R.string.signup_request_error)
                t.printStackTrace()
            }
        })
    }

    override fun signUp(user: User) {
        val posts: Call<User> = retrofit.create(ApiRequest::class.java).createUser(user)
        posts.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        loginView.saveUser(response.body()!!)
                        return
                    }
                    loginView.showFeedBack(R.string.signup_request_error)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                loginView.showFeedBack(R.string.signup_request_error)
                t.printStackTrace()
            }
        })
    }

    override fun onAuthComplete(isNewUser: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}