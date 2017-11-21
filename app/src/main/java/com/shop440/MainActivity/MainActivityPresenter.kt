package com.shop440.MainActivity

import com.google.common.collect.ImmutableList
import com.shop440.Adapters.ViewModel.ProductViewModel
import com.shop440.Adapters.ViewModel.StoreViewModel
import com.shop440.Adapters.ViewModel.ViewModel
import com.shop440.models.Page
import com.shop440.models.ProductModel
import com.shop440.R
import com.shop440.models.Feed
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

    override fun getProductFeedData() {
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
                    mainActivityFragmentView.productDataAvailable(parseViewModels(response.body()))
                } else {
                    mainActivityFragmentView.onError(R.string.api_data_load_error)
                }
            }
        })
    }

    private fun parseViewModels(section: SectionResponse.HomeSection?): List<ViewModel> {
        val listOfModel = mutableListOf<ViewModel>()
        if(section != null){
            for (sectionRes in section.sections) {
                listOfModel.add(selectType(sectionRes))
            }
        }else{
            mainActivityFragmentView.onError(R.string.feed_error_data)
        }
        return listOfModel
    }

    private fun selectType(sectionResponse: SectionResponse.SectionResponse): ViewModel{
        return if (sectionResponse.feedType == "something") {
                ProductViewModel(sectionResponse.title, sectionResponse.productFeed)
        } else {
                StoreViewModel(sectionResponse.title, sectionResponse.shopFeed)
        }
    }

}