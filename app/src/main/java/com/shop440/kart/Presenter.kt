package com.shop440.kart

import android.arch.lifecycle.Observer
import android.util.Log
import com.shop440.dao.models.ProductFeed
import io.realm.RealmResults

/**
 * Created by mmumene on 03/02/2018.
 */
class Presenter(val view: KartContract.View) : KartContract.Presenter {
    protected val viewModel by lazy {
        view.getViewModel()
    }

    override fun loadKart(activity: BaseKartActivity) {
        viewModel.getKartData().observe(activity, Observer<RealmResults<Item>> { t ->
            Log.i("presenter", t?.size.toString())
            view.onKartLoaded(t)
        })
    }

    override fun addToKart(product: ProductFeed) {
        viewModel.addToKart(product)
    }

    override fun addToKart(itemForKart: ItemForKart) {
        viewModel.addToKart(itemForKart)
    }

    override fun deleteAll(slug: String) {
        viewModel.deleteAll(slug)
    }

    override fun deleteFromKart(id: String) {
        viewModel.deleteFromKart(id)
    }

    override fun getItem(id: String) {

    }

    override fun start() {

    }
}