package com.shop440.checkout

import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.util.Log
import com.shop440.checkout.kart.KartContract
import com.shop440.checkout.models.Item
import com.shop440.checkout.models.ItemForKart
import com.shop440.checkout.models.SummaryAdapterModel
import com.shop440.navigation.home.adaptermodel.AdapterModel
import io.realm.RealmResults

/**
 * Created by mmumene on 11/03/2018.
 */
class OrderPresenter(val view : KartContract.OrderView) : KartContract.OrderPresenter{
    init {
        view.presenter = this
    }
    private val viewModel by lazy {
        view.getViewModel()
    }

    override fun loadKart(fragment: Fragment) {
        viewModel.getKartData().observe(fragment, Observer<RealmResults<Item>> { t ->
            var total = 0.0
            val kartItem = computeRealmKartItem(t!!)
            for(item in kartItem.values){
                total += item.amount
            }
            view.onKartLoaded(getAdapterModels(kartItem), total)
        })
    }

    override fun start() {

    }

    private fun computeShopNames(items:HashMap<String, ItemForKart>) =  items.values.mapTo(HashSet(), { keys ->
        keys.shopName
    })

    private fun computeRealmKartItem(realmResults: RealmResults<Item>) : HashMap<String, ItemForKart> {
        val map = HashMap<String, ItemForKart>()
        realmResults.let {
            for (results in it) {
                if (!map.containsKey(results.itemName)) {
                    map[results.itemName] = ItemForKart(results.itemName, results.shopName, results.slug, results.id, results.shopSlug)
                }
                map[results.itemName]?.apply {
                    amount = amount.plus(results.totalPrice)
                    quantity = map[results.itemName]?.quantity?.plus(1)!!
                    item = results
                }
            }
        }
        return map
    }

    private fun getAdapterModels(map:HashMap<String, ItemForKart>):ArrayList<AdapterModel>{
        val shopNames = computeShopNames(map)
        val models = arrayListOf<AdapterModel>()
        for (key in shopNames.toList()) {
            val itemList = arrayListOf<ItemForKart>()
            var totalPerShop = 0.0
            for ((_, value) in map) {
                if (value.shopName == key) {
                    itemList.add(value)
                    //for now no shipping so itemCost and total are the same
                    totalPerShop.plus(value.amount)
                }
            }
            models.add(SummaryAdapterModel(key, itemList))
        }
        return models
    }


    /*  for (key in shopNames.toList()) {
          val itemList = arrayListOf<ItemForKart>()
          var totalPerShop = 0.0
          for ((_, value) in map) {
              if (value.shopName == key) {
                  itemList.add(value)
                  //for now no shipping so itemCost and total are the same
                  totalPerShop.plus(value.amount)
              }
          }
          val shop = ShopOrders().apply {
              shopName = itemList[0].shopName
              shopId = itemList[0].shopSlug
              total = totalPerShop
              itemList.mapTo(items, { t->
                  t.item
              })
          }
          shopOrders.add(shop)
          viewModel.add(SummaryAdapterModel(key, itemList))
      }
  }
}*/

}