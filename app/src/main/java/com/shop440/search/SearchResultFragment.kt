package com.shop440.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.shop440.R
import com.shop440.dao.models.Product
import com.shop440.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_main.*

class SearchResultFragment : Fragment() {
    lateinit var layoutManager: StaggeredGridLayoutManager
    var next: Boolean? = true
    val TAG = "SearchResultActivity"
    val searchControl : SearchContainerActivity by lazy {
        activity as SearchContainerActivity
    }
    private val list = mutableListOf<Product>()
    val adapter : SearchResultAdapter by lazy {
        SearchResultAdapter(Glide.with(context), list)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.searchresult, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )

        swipeContainer.setOnRefreshListener {
            list.clear()
            searchControl.presenter.performSearch(searchControl.queryString, "1", searchControl.catString, "")
        }
        recyclerView.apply{
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            adapter = this@SearchResultFragment.adapter
            setHasFixedSize(true)

        }.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d(TAG, page.toString())
                if (searchControl.next) {
                    searchControl.presenter.performSearch(searchControl.queryString, page.toString(), searchControl.catString, "")
                }
            }
        })
    }

    fun refreshLayout(products:List<Product>){
        list.addAll(products)
        adapter.notifyDataSetChanged()
    }
}
