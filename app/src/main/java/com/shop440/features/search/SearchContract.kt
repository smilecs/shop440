package com.shop440.features.search

import android.arch.lifecycle.LifecycleOwner
import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.repository.dao.models.CategoryModel
import com.shop440.repository.dao.models.Page
import com.shop440.navigation.home.adaptermodel.ProductModel

/**
 * Created by mmumene on 25/02/2018.
 */
interface SearchContract{

    interface View : BaseView<Presenter>{
        fun onSearchResults(productModel: ProductModel, page: Page)
        fun onCategories(categories:ArrayList<CategoryModel>)
        val lifeCycle : LifecycleOwner
    }

    interface Presenter : BasePresenter{
        fun performSearch(q:String, p:String, cat:String, tag:String)
        fun getCategories()
    }
}