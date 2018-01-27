package com.shop440.search

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.widget.TextView
import com.shop440.R
import com.shop440.api.Urls
import com.shop440.dao.models.Datum
import com.shop440.productview.adapter.ProductAdapter
import com.shop440.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.searchresult.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class SearchResultActivity : AppCompatActivity() {
    lateinit var query: String
    lateinit var page: String
    lateinit var list: RecyclerView
    lateinit var mainAdapter: ProductAdapter
    lateinit var model: ArrayList<Datum>
    lateinit var c: Context
    lateinit var layoutManager: StaggeredGridLayoutManager
    lateinit var refreshLayout: SwipeRefreshLayout
    var next: Boolean? = true
    lateinit var feedback: TextView
    val TAG = "SearchResultActivity"
    lateinit var URI: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchresult)
        c = this
        setSupportActionBar(toolbar)
        query = intent.getStringExtra("query")
        supportActionBar!!.setTitle(query)
        if (intent.hasExtra("title")) {
            supportActionBar!!.setTitle(intent.getStringExtra("title"))
        }
        page = "1"
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )

        refreshLayout.setOnRefreshListener { }
        model = ArrayList()
        mainAdapter = ProductAdapter(c, model)
        list = findViewById<RecyclerView>(R.id.recyclerView)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        list.setHasFixedSize(true)
        list.layoutManager = layoutManager
        list.adapter = mainAdapter
        list.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d(TAG, page.toString())
                if (next!!) {
                }
            }
        })
    }


    fun getUrl(page: String, q: String): String {
        if (intent.getBooleanExtra("isSearch", true)) {
            try {
                URI = Urls.BASE_URL + "url" + "?query=" + URLEncoder.encode(q, "UTF-8") + "&p=" + page
            } catch (un: UnsupportedEncodingException) {
                un.printStackTrace()
            }

        } else {
            try {
                URI = Urls.BASE_URL + "url" + "?category=" + URLEncoder.encode(q, "UTF-8") + "&p=" + page
            } catch (un: UnsupportedEncodingException) {
                un.printStackTrace()
            }

        }
        return URI
    }
}
