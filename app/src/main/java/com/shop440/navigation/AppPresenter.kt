package com.shop440.navigation

import com.shop440.models.CategoryModel
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 31/12/2017.
 */
class AppPresenter(val retrofit: Retrofit, val view: AppContract.AppView) : AppContract.Presenter {
    init {
        view.presenter = this
    }

    override fun start() {
        val realm = Realm.getDefaultInstance()
        realm.where(CategoryModel::class.java).findAllAsync().addChangeListener { t, changeSet ->
            if (t.isEmpty()) {
                view.onDataLoading()
                refreshCategories(realm)
            }
        }
    }

    private fun refreshCategories(realm: Realm) {
        val categoryData: Call<List<CategoryModel>> = retrofit.create(ApiRequest::class.java).getCategories()
        categoryData.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(call: Call<List<CategoryModel>>?, response: Response<List<CategoryModel>>?) {
                view.onDataLoading()
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.let {
                            realm.executeTransactionAsync { obj: Realm? ->
                                obj?.insert(it)
                            }
                        }
                    }
                    realm.removeAllChangeListeners()
                }
            }

            override fun onFailure(call: Call<List<CategoryModel>>?, t: Throwable?) {
                view.onDataLoading()
                t?.printStackTrace()
            }
        })
    }
}