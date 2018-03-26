package com.shop440.features.productview

import android.arch.lifecycle.Observer
import com.shop440.R
import com.shop440.features.checkout.models.Item
import com.shop440.features.checkout.models.ShopOrders
import com.shop440.repository.dao.models.CategoryModel
import com.shop440.repository.dao.models.Product
import io.realm.Realm
import io.realm.RealmObjectChangeListener
import io.realm.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by mmumene on 09/09/2017.
 */
class ProductViewPresenter(val productView: ProductViewContract.View?, val retrofit: Retrofit) : ProductViewContract.Presenter {

   private val productViewModel by lazy {
        productView?.getViewModel()
    }

    init {
        productView?.presenter = this
    }

    override fun start() {

    }

    override fun loadData(path: String) {
        productView?.onDataLoading()
        val data: Call<Product> = retrofit.create(ApiRequest::class.java).getProduct(path, "")
        data.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>?, response: Response<Product>?) {
                if (response!!.isSuccessful) {
                    productView?.onDataLoading()
                    if (response.body() != null) {
                        productView?.showProduct(response.body()!!)
                        return
                    }
                    productView?.onError(R.string.api_data_load_error)
                } else {
                    productView?.onDataLoading()
                    productView?.onError(R.string.api_data_load_error)
                }
            }

            override fun onFailure(call: Call<Product>?, t: Throwable?) {
                productView?.onDataLoading()
                productView?.onError(R.string.internet_error_message)
            }
        })
    }

    override fun loadCart(activity: ProductViewActivity) {
       productViewModel?.getKartData()?.observe(activity, Observer<RealmResults<Item>> { t ->
            productView?.cartLoaded(t)
        })
    }

    override fun resolveCategory(slug: String) {
        val realm = Realm.getDefaultInstance()
        realm.where(CategoryModel::class.java).equalTo("slug", slug).findFirstAsync().addChangeListener(RealmObjectChangeListener<CategoryModel> { t, changeSet ->
            t.let {
                if (t.isLoaded) {
                    productView?.categoryNameResolved(t.catName)
                    realm.close()
                }
            }
        })
    }

    override fun addToCart(product: Product) {
       productViewModel?.addToKart(product)
    }

    //check usefullness of this method, might be pointless
    override fun getShopOrder(shopId: String) {
        Realm.getDefaultInstance().use {
            it.where(ShopOrders::class.java).equalTo("shopId", shopId).findFirstAsync().addChangeListener(RealmObjectChangeListener<ShopOrders> { t, changeSet ->
                if (t.isLoaded) {
                    it.removeAllChangeListeners()
                    productView?.shopOrder(t)
                }
            })
        }
    }
}