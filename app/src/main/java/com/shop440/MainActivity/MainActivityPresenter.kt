package com.shop440.MainActivity

import com.shop440.models.Page
import com.shop440.models.ProductModel
import com.shop440.R
import com.shop440.response.SectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 09/09/2017.
 */

class MainActivityPresenter(val mainActivityFragmentView: MainActivityContract.View, val retrofit: Retrofit) : MainActivityContract.Presenter {
    init {
        mainActivityFragmentView.presenter = this
    }

    override fun start() {
    }

    override fun getProductFeedData(page: Page) {
        mainActivityFragmentView.onDataLoading()
        val productData: Call<SectionResponse.HomeSection> = retrofit.create(ApiRequest::class.java).homeSection()
        productData.enqueue(object : Callback<SectionResponse.HomeSection> {
            init {
                mainActivityFragmentView.onDataLoading()
            }

            override fun onFailure(call: Call<SectionResponse.HomeSection>?, t: Throwable?) {
                mainActivityFragmentView.onError(R.string.internet_error_message)
            }

            override fun onResponse(call: Call<SectionResponse.HomeSection>?, response: Response<SectionResponse.HomeSection>?) {
                if (response?.isSuccessful!!) {
                    mainActivityFragmentView.productDataAvailable(response.body())
                } else {
                    mainActivityFragmentView.onError(R.string.api_data_load_error)
                }
            }
        })
    }
}