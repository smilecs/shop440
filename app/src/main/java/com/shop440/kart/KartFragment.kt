package com.shop440.kart

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.checkout.CheckoutActivity
import io.realm.RealmResults


class KartFragment : Fragment(), KartContract.View, KartAdapter.OnListFragmentInteractionListener {
    // TODO: Customize parameters

    override lateinit var presenter: KartContract.Presenter
    private val kartItems: MutableList<ItemForKart> by lazy {
        arrayListOf<ItemForKart>()
    }
    private val kartAdapter: KartAdapter by lazy {
        KartAdapter(kartItems as ArrayList<ItemForKart>, this)
    }

    private val kartViewModel: KartViewModel by lazy {
        ViewModelProviders.of(this).get(KartViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Presenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            view.setHasFixedSize(true)
            view.adapter = kartAdapter
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadKart(activity as CheckoutActivity)
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun onKartLoaded(realmResults: RealmResults<Item>?) {
        val map = HashMap<String, ItemForKart>()
        realmResults?.let {
            kartItems.clear()
            for (results in it) {
                if (!map.containsKey(results.itemName)) {
                    map.put(results.itemName, ItemForKart(results.itemName, results.shopName, results.slug, results.id, results.shopSlug))
                }
                map[results.itemName]?.apply {
                    amount = amount.plus(results.totalPrice)
                    quantity = map[results.itemName]?.quantity?.plus(1)!!
                }
            }
            kartItems.addAll(map.values)
            kartAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemAvailable(item: Item) {

    }

    override fun onItemDeleted() {

    }

    override fun getViewModel() = kartViewModel


    //adapter interfaces
    override fun onListFragmentInteraction(item: ItemForKart) {

    }

    override fun onItemDelete(item: ItemForKart) {
        presenter.deleteAll(item.slug)
    }

    override fun onItemIncrement(item: ItemForKart, increment: Boolean) {
        if(increment){
            presenter.addToKart(item)
        }else{
            presenter.deleteFromKart(item.id)
        }
    }
}
