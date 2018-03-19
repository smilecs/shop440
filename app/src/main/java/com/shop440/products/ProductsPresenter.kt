package com.shop440.products

import android.arch.lifecycle.Observer
import com.shop440.R
import com.shop440.dao.models.CategoryModel
import com.shop440.navigation.home.adaptermodel.ProductModel
import com.shop440.api.response.FilterResponse
import com.shop440.api.response.ResponseWrapper
import io.realm.Realm
import io.realm.RealmObjectChangeListener

/**
 * Created by mmumene on 17/03/2018.
 */

class ProductsPresenter(val view: ProductsContract.View?) : ProductsContract.Presenter {

    init {
        view?.presenter = this
    }

    override fun start() {
        view?.let {
            it.viewModel.productLiveData().observe(it.lifecycleOwner, Observer<ResponseWrapper<FilterResponse>> {
                view.onDataLoading()
                when (it?.error) {
                    true -> view.onError(R.string.api_data_load_error)
                    else -> it?.resp?.let {
                        view.onProductsLoaded(ProductModel(it.query, it.data), it.page)
                    }
                }
            })
        }
    }

    override fun loadProducts(q: String, p: String, cat: String, tag: String) {
        view?.let {
            it.onDataLoading()
            it.viewModel.getResponse(q, p, cat, tag)
        }
    }

    override fun resolveCategories(slug: String) {
        val realm = Realm.getDefaultInstance()
        realm.let {
            it.where(CategoryModel::class.java).equalTo("slug", slug).findFirstAsync().addChangeListener(RealmObjectChangeListener<CategoryModel> { t, changeSet ->
                t.let {
                    if (t.isLoaded) {
                        view?.onCategoryResolved(t.catName)
                        realm.close()
                    }
                }
            })
        }
    }
}