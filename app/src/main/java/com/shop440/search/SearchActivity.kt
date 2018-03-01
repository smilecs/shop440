package com.shop440.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import com.shop440.R
import com.shop440.adapters.CategorySearchAdapter
import com.shop440.dao.models.CategoryModel
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : Fragment() {
    private var model: ArrayList<CategoryModel>? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var categoryAdapter: CategorySearchAdapter? = null
    private var bar: ProgressBar? = null
    private var searchItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ArrayList()
        categoryAdapter = CategorySearchAdapter(this, model)
        bar = R.id.progressBar
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = categoryAdapter
    }
}
