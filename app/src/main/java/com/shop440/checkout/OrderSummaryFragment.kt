package com.shop440.checkout

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.checkout.kart.KartContract
import com.shop440.checkout.kart.KartViewModel
import com.shop440.checkout.kart.Presenter
import com.shop440.checkout.models.Item
import com.shop440.checkout.models.ItemForKart
import com.shop440.checkout.models.SummaryViewModel
import com.shop440.adapters.TopFeedAdapter
import com.shop440.navigation.home.viewmodel.ViewModel
import com.shop440.view.ItemDecorator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_order_summary.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OrderSummaryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OrderSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderSummaryFragment : Fragment(), KartContract.View {

    val viewModel = arrayListOf<ViewModel>()
    override lateinit var presenter: KartContract.Presenter
    private val kartViewModel: KartViewModel by lazy {
        ViewModelProviders.of(this).get(KartViewModel::class.java)
    }

    private val modelAdapter by lazy {
        TopFeedAdapter(viewModel, context, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Presenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_summary, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = modelAdapter
            addItemDecoration(ItemDecorator(context, LinearLayoutManager.VERTICAL))
        }
        presenter.loadKart(activity as CheckoutActivity)
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun onKartLoaded(realmResults: RealmResults<Item>?) {
        val map = HashMap<String, ItemForKart>()
        val shopNames = HashSet<String>()
        realmResults?.let {
            viewModel.clear()
            for (results in it) {
                if (!map.containsKey(results.itemName)) {
                    map.put(results.itemName, ItemForKart(results.itemName, results.shopName, results.slug, results.id, results.shopSlug))
                }
                map[results.itemName]?.apply {
                    amount = amount.plus(results.totalPrice)
                    quantity = map[results.itemName]?.quantity?.plus(1)!!
                }
                if (!shopNames.contains(results.shopName)) {
                    shopNames.add(results.shopName)
                }
            }

            for (key in shopNames.toList()) {
                val itemList = arrayListOf<ItemForKart>()
                for ((_, value) in map) {
                    if (value.shopName == key) {
                        itemList.add(value)
                    }
                }
                viewModel.add(SummaryViewModel(key, itemList))
            }
        }
        modelAdapter.notifyDataSetChanged()
    }

    override fun onItemAvailable(item: Item) {

    }

    override fun onItemDeleted() {

    }

    override fun getViewModel() = kartViewModel

}
