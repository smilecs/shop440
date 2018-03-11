package com.shop440.checkout

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.adapters.TopFeedAdapter
import com.shop440.checkout.kart.KartContract
import com.shop440.viewmodel.KartViewModel
import com.shop440.checkout.kart.Presenter
import com.shop440.checkout.models.Item
import com.shop440.checkout.models.ItemForKart
import com.shop440.checkout.models.ShopOrders
import com.shop440.checkout.models.SummaryAdapterModel
import com.shop440.navigation.home.adaptermodel.AdapterModel
import com.shop440.utils.Metrics
import com.shop440.view.ItemDecorator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_order_summary.*


class OrderSummaryFragment : Fragment(), KartContract.OrderView {

    val viewModel = arrayListOf<AdapterModel>()
    var amountForTotal = 0.0
    override lateinit var presenter: KartContract.OrderPresenter
    val shopOrders = arrayListOf<ShopOrders>()
    private val kartViewModel: KartViewModel by lazy {
        ViewModelProviders.of(this).get(KartViewModel::class.java)
    }

    private val modelAdapter by lazy {
        TopFeedAdapter(viewModel, context, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OrderPresenter(this)
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
        presenter.loadKart(this)
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    //pasta do not modify anyhow!!!!!
    override fun onKartLoaded(items: ArrayList<AdapterModel>, total: Double) {
        viewModel.let {
            it.clear()
            it.addAll(items)
        }
        modelAdapter.notifyDataSetChanged()

        this@OrderSummaryFragment.total.text = Metrics.getDisplayPriceWithCurrency(context, amountForTotal)

    }

    override fun getViewModel() = kartViewModel

}
