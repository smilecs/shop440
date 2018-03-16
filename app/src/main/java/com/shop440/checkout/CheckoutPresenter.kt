package com.shop440.checkout

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.shop440.R
import com.shop440.checkout.models.Order
import com.shop440.resp.GenericResponse
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

    override fun checkOut(order: Order, lifecycle: LifecycleOwner) {
        view.onDataLoading()
        viewModel.persistOrder(order).observe(lifecycle, Observer<Order> {t ->
            t?.let {
                val call : Call<GenericResponse> = retrofit.create(ApiRequest::class.java).newOrder(it)
                call.enqueue(object : Callback<GenericResponse> {
                    override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                        view.onDataLoading()
                        if(response?.body() != null){
                            view.onCheckOut()
                            viewModel.clearKart()
                            return
                        }
                        onFailure(call, null)
                    }

                    override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                        view.onDataLoading()
                        t?.printStackTrace()
                        view.onError(R.string.api_data_load_error)
                    }
                })
            }
        })
    }
}