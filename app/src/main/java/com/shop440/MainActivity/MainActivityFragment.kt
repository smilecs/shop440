package com.shop440.MainActivity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.Adapters.TopFeedAdapter
import com.shop440.Adapters.ViewModel.ViewModel
import com.shop440.R
import com.shop440.api.NetModule
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), MainActivityContract.View {
    override lateinit var presenter: MainActivityContract.Presenter
    private lateinit var mainAdapter : TopFeedAdapter
    private val model = mutableListOf<ViewModel>()
    private val c: Context by lazy {
        context
    }
    private val TAG = "MainActivityFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var token: String
    lateinit var viewRoot: View
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity.getSharedPreferences(resources.getString(R.string.shop440), MODE_PRIVATE)
        token = sharedPreferences.getString("token", "null")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewRoot = inflater!!.inflate(R.layout.fragment_main, container, false)
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
        MainActivityPresenter(this, NetModule.provideRetrofit())
        mainAdapter = TopFeedAdapter(model, c)
        swipeContainer.setOnRefreshListener {
            getProducts()
        }
        layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mainAdapter
        return viewRoot
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProducts()
    }

    override fun onError(errorMessage: Int) {
        feedback.visibility = View.VISIBLE
        Snackbar.make(viewRoot, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        swipeContainer.isRefreshing = !swipeContainer.isRefreshing
    }

    override fun productDataAvailable(homeSection: List<ViewModel>) {
        model.clear()
        model.addAll(homeSection)
        mainAdapter.notifyDataSetChanged()
    }

    private fun getProducts(){
        presenter.getProductFeedData()
    }
}
