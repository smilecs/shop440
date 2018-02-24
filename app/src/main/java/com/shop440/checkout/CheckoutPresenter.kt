package com.shop440.checkout

import com.shop440.R
import com.shop440.checkout.models.Order
import com.shop440.response.GenericResponse
import com.shop440.viewmodel.KartViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 03/02/2018.
 */
class CheckoutPresenter(val view: CheckoutContract.View, val retrofit: Retrofit) : CheckoutContract.Presenter {
    private val viewModel: KartViewModel by lazy {
        view.getViewModel() as KartViewModel
    }

    init {
        view.presenter = this
    }

    override fun start() {

    }

    override fun checkOut(order: Order) {
        view.onDataLoading()
        val call = retrofit.create(ApiRequest::class.java).newOrder(order)
        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                view.onDataLoading()
                if(response?.body() != null){
                    view.onCheckOut()
                    viewModel.persistOrder(order)
                    return
                }
                onFailure(call, null)
            }

            override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                view.onDataLoading()
                view.onError(R.string.api_data_load_error)
            }
        })

    }
}