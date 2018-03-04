package com.shop440.search

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.Observer
import com.shop440.api.NetModule
import com.shop440.navigation.home.adaptermodel.ProductModel
import com.shop440.response.FilterResponse
import com.shop440.viewmodel.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 25/02/2018.
 */

class SearchPresenter(val view: SearchContract.View?) : SearchContract.Presenter {
    init {
        view?.presenter = this
    }
    private val productViewModel: ProductViewModel by lazy {
        ProductViewModel()
    }

    private val retrofit:Retrofit by lazy {
        NetModule.provideRetrofit()
    }

    override fun start() {

    }

    override fun getCategories() {
        view?.let {
            productViewModel.getCategories().observe(view.lifeCycle, Observer { t ->
                view.onCategories(t?.toList() as ArrayList)
            })
        }
    }

    override fun performSearch(q: String, p: String, cat: String, tag: String) {
        val call = retrofit.create(ApiRequest::class.java).getFilterList(q, p, cat, tag)
        call.enqueue(object : Callback<FilterResponse> {
            override fun onResponse(call: Call<FilterResponse>?, response: Response<FilterResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            view?.onSearchResults(createSearchModel(q, it))
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

    private fun createSearchModel(query:String, filterResponse: FilterResponse) = ProductModel(query, filterResponse.data)
}