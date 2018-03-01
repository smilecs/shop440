package com.shop440.navigation.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.shop440.adapters.TopFeedAdapter
import com.shop440.navigation.home.viewmodel.ViewModel
import com.shop440.R
import com.shop440.api.NetModule
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class HomeActivityFragment : Fragment(), HomeActivityContract.View {
    override lateinit var presenter: HomeActivityContract.Presenter
    private lateinit var mainAdapter: TopFeedAdapter
    private val model = mutableListOf<ViewModel>()
    private val c: Context by lazy {
        context
    }
    private val TAG = "MainActivityFragment"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity.getSharedPreferences(resources.getString(R.string.shop440), MODE_PRIVATE)
        token = sharedPreferences.getString("token", "null")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewRoot = inflater?.inflate(R.layout.fragment_main, container, false)

        HomeActivityPresenter(this, NetModule.provideRetrofit())
        mainAdapter = TopFeedAdapter(model, c)
        return viewRoot
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = searchViewQuery.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        editText.setHintTextColor(resources.getColor(R.color.colorAccent))
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
        swipeContainer.setOnRefreshListener {
            onDataLoading()
            getProducts()
        }
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        getProducts()
    }

    override fun onError(errorMessage: Int) {
        if (isVisible) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDataLoading() {
        swipeContainer?.isRefreshing = !swipeContainer.isRefreshing
    }

    override fun productDataAvailable(homeSection: List<ViewModel>) {
        model.clear()
        model.addAll(homeSection)
        mainAdapter.notifyDataSetChanged()
    }

    private fun getProducts() {
        presenter.getProductFeedData()
    }
}
