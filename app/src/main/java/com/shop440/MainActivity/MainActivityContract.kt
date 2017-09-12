package com.shop440.MainActivity

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.Models.Page
import com.shop440.Models.ProductModel

/**
 * Created by mmumene on 09/09/2017.
 */

interface MainActivityContract{
    interface View : BaseView<Presenter>{
        fun productDataAvailable(productModel: ProductModel)
    }

    interface Presenter : BasePresenter {
        fun getProductFeedData(page:Page)
    }
}
