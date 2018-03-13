package com.shop440.navigation.home

import com.shop440.navigation.home.adaptermodel.ProductModel
import com.shop440.navigation.home.adaptermodel.StoreAdapterModel
import com.shop440.navigation.home.adaptermodel.AdapterModel
import com.shop440.R
import com.shop440.response.SectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 09/09/2017.
 */

class HomeActivityPresenter(val homeActivityFragmentView: HomeActivityContract.View, val retrofit: Retrofit) : HomeActivityContract.Presenter {
    init {
        homeActivityFragmentView.presenter = this
    }

    override fun start() {
    }

    override fun getProductFeedData() {
        homeActivityFragmentView.onDataLoading()
        val productData: Call<SectionResponse.HomeSection> = retrofit.create(ApiRequest::class.java).homeSection()
        productData.enqueue(object : Callback<SectionResponse.HomeSection> {

            override fun onFailure(call: Call<SectionResponse.HomeSection>?, t: Throwable?) {
                homeActivityFragmentView.onDataLoading()
                homeActivityFragmentView.onError(R.string.internet_error_message)
            }

            override fun onResponse(call: Call<SectionResponse.HomeSection>?, response: Response<SectionResponse.HomeSection>?) {
                homeActivityFragmentView.onDataLoading()
                if (response?.isSuccessful!!) {
                    homeActivityFragmentView.productDataAvailable(parseViewModels(response.body()))
                } else {
                    homeActivityFragmentView.onError(R.string.api_data_load_error)
                }
            }
        })
    }

    private fun parseViewModels(section: SectionResponse.HomeSection?): List<AdapterModel> {
        val listOfModel = mutableListOf<AdapterModel>()
        if (section != null) {
            section.sections.mapTo(listOfModel, { t ->
                selectType(t)
            })
        } else {
            homeActivityFragmentView.onError(R.string.feed_error_data)
        }
        return listOfModel
    }

    private fun selectType(sectionResponse: SectionResponse.SectionResponse): AdapterModel {
        return when (sectionResponse.feedType) {
            "productfeed" -> ProductModel(sectionResponse.title, sectionResponse.product)
            else -> StoreAdapterModel(sectionResponse.title, sectionResponse.shopFeed)
        }
    }

}