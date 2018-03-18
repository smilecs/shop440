package com.shop440.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.adapters.CategorySearchAdapter
import com.shop440.dao.models.CategoryModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchFragment : Fragment(), CategorySearchAdapter.CategoryQuery {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_search, container, false)
    }

    fun onCategories(categories:ArrayList<CategoryModel>){
        val adapter = CategorySearchAdapter(context, categories)
        adapter.categoryQuery = this
        progressBar.visibility = View.GONE
        recyclerView.run {
            this.adapter = adapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCategorySelected(cat: String) {
        (activity as SearchContainerActivity).catString = cat
        (activity as SearchContainerActivity).startSearch("", "1", cat, "")
    }
}
