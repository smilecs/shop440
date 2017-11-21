package com.shop440.MainActivity

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.models.Page
import com.shop440.models.ProductModel
import com.shop440.response.SectionResponse

/**
 * Created by mmumene on 09/09/2017.
 */

interface MainActivityContract{
    interface View : BaseView<Presenter>{
        fun productDataAvailable(homeSection: SectionResponse.HomeSection?)
    }

    interface Presenter : BasePresenter {
        fun getProductFeedData(page:Page)
    }
}
