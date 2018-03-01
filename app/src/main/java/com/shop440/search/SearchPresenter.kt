package com.shop440.search

import com.shop440.response.FilterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 25/02/2018.
 */

class SearchPresenter(val view: SearchContract.View?,
                      val retrofit: Retrofit) : SearchContract.Presenter {
    override fun start() {

    }

    override fun performSearch(q: String, p: String, cat: String, tag: String) {
        val call = retrofit.create(ApiRequest::class.java).getFilterList(q, p, cat, tag)
        call.enqueue(object : Callback<FilterResponse> {
            override fun onResponse(call: Call<FilterResponse>?, response: Response<FilterResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            view?.onSearchResults(it)
                        }
                    } else {
                        onFailure(call, null)
                    }
                }
            }

            override fun onFailure(call: Call<FilterResponse>?, t: Throwable?) {

            }
        })
    }
}