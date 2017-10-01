package com.shop440.MainActivity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shop440.Adapters.ProductAdapter
import com.shop440.Api.NetModule
import com.shop440.Models.Datum
import com.shop440.Models.Page
import com.shop440.Models.ProductModel
import com.shop440.R
import com.shop440.Utils.EndlessRecyclerViewScrollListener
import com.shop440.Utils.Metrics
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), MainActivityContract.View {
    override lateinit var presenter: MainActivityContract.Presenter
    lateinit var list: RecyclerView
    lateinit var mainAdapter: ProductAdapter
    lateinit var model: MutableList<Datum>
    lateinit var c: Context
    var TAG = "MainActivityFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var token: String
    lateinit var viewRoot: View
    lateinit var feedback: TextView
    lateinit var layoutManager: StaggeredGridLayoutManager
    lateinit var refreshLayout: SwipeRefreshLayout
    var next: Boolean = true
    var page = Page()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        c = activity
        sharedPreferences = activity.getSharedPreferences(resources.getString(R.string.shop440), MODE_PRIVATE)
        token = sharedPreferences.getString("token", "null")
        model = ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewRoot = inflater!!.inflate(R.layout.fragment_main, container, false)
        refreshLayout = viewRoot.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
        MainActivityPresenter(this, NetModule.provideRetrofit())
        mainAdapter = ProductAdapter(c, model)
        refreshLayout.setOnRefreshListener {
            page.nextVal = 1
            getProducts()
        }
        list = viewRoot.findViewById(R.id.recyclerView) as RecyclerView
        feedback = viewRoot.findViewById(R.id.feedback) as TextView
        layoutManager = StaggeredGridLayoutManager(Metrics.GetMetrics(list, activity), StaggeredGridLayoutManager.VERTICAL)
        list.setHasFixedSize(true)
        list.layoutManager = layoutManager
        list.adapter = mainAdapter
        list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d(TAG, page.toString())
                if (this@MainActivityFragment.page.next) {
                    getProducts()
                }

            }
        })
        return viewRoot
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        page.nextVal = 1
        getProducts()
    }

    override fun onError(errorMessage: Int) {
        feedback.visibility = View.VISIBLE
        Snackbar.make(viewRoot, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        refreshLayout.isRefreshing = refreshLayout.isRefreshing
    }

    override fun productDataAvailable(productModel: ProductModel) {
        page = productModel.page
        model.addAll(productModel.data)
        mainAdapter.notifyDataSetChanged()
    }

    fun getProducts(){
        presenter.getProductFeedData(page)
    }
}
