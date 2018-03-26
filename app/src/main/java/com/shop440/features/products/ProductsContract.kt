package com.shop440.features.products

import android.arch.lifecycle.LifecycleOwner
import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.repository.dao.models.Page
import com.shop440.navigation.home.adaptermodel.ProductModel

/**
 * Created by mmumene on 17/03/2018.
 */
interface ProductsContract{

    interface View:BaseView<Presenter>{
        fun onProductsLoaded(productModel: ProductModel, page: Page)
        val viewModel : com.shop440.features.products.ViewModel
        fun onCategoryResolved(category:String)
        val lifecycleOwner:LifecycleOwner
    }

    interface Presenter : BasePresenter{
        fun loadProducts(q:String, p:String, cat:String, tag:String)
        fun resolveCategories(slug:String)
    }
}