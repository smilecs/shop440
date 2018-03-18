package com.shop440.products

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.shop440.api.NetModule
import com.shop440.resp.FilterResponse
import com.shop440.resp.ResponseWrapper
import com.shop440.search.ApiRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 17/03/2018.
 */
class ViewModel : ViewModel() {

    private val retrofit: Retrofit by lazy {
        NetModule.provideRetrofit()
    }

    private var filterResponse = MutableLiveData<ResponseWrapper<FilterResponse>>()

    fun productLiveData(): LiveData<ResponseWrapper<FilterResponse>> = filterResponse


    fun getResponse(q:String, p:String, cat:String, tag:String){
        val call = retrofit.create(ApiRequest::class.java).getFilterList(q, p, cat, tag)
        call.enqueue(object : Callback<FilterResponse> {
            override fun onResponse(call: Call<FilterResponse>?, response: Response<FilterResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            val responseWrapper = ResponseWrapper<FilterResponse>().apply {
                                error = false
                                resp = it
                            }
                            filterResponse.value = responseWrapper
                        }
                    } else {
                        onFailure(call, null)
                    }
                }
            }

            override fun onFailure(call: Call<FilterResponse>?, t: Throwable?) {
                val responseWrapper = ResponseWrapper<FilterResponse>().apply {
                    error = false
                }
                filterResponse.value = responseWrapper
            }
        })
    }
}