package com.shop440.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.shop440.R
import com.shop440.adapters.CategorySearchAdapter
import com.shop440.dao.models.CategoryModel
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {
    private var model: ArrayList<CategoryModel>? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var categoryAdapter: CategorySearchAdapter? = null
    private var bar: ProgressBar? = null
    private var searchItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        model = ArrayList()
        categoryAdapter = CategorySearchAdapter(this, model)
        bar = findViewById<ProgressBar>(R.id.progressBar)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = categoryAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        //MenuItem searchItem = menu.findItem(R.id.search);
        searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        val searchEditId = android.support.v7.appcompat.R.id.search_src_text
        searchItem!!.expandActionView()
        val et = searchView.findViewById<View>(searchEditId) as EditText
        et.setTextColor(Color.BLACK)
        et.setHintTextColor(Color.GRAY)
        et.requestFocus()
        searchView.isIconified = false
        searchView.setIconifiedByDefault(true)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val i = Intent(this@SearchActivity, SearchResultActivity::class.java)
                i.putExtra("query", query)
                i.putExtra("isSearch", true)
                startActivity(i)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

}
