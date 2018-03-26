package com.shop440.features.checkout.kart

import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import com.shop440.features.checkout.models.Item
import com.shop440.features.checkout.models.ItemForKart
import com.shop440.repository.dao.models.Product
import io.realm.RealmResults

/**
 * Created by mmumene on 03/02/2018.
 */
class Presenter(val view: KartContract.View) : KartContract.Presenter {
    init {
        view.presenter = this
    }
    private val viewModel by lazy {
        view.getViewModel()
    }

    override fun loadKart(fragment: Fragment) {
        viewModel.getKartData().observe(fragment, Observer<RealmResults<Item>> { t ->
            view.onKartLoaded(t)
        })
    }

    override fun addToKart(product: Product) {
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