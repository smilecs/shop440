package com.shop440.search

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.widget.EditText
import com.shop440.R
import com.shop440.dao.models.CategoryModel
import com.shop440.dao.models.Page
import com.shop440.navigation.home.adaptermodel.ProductModel
import kotlinx.android.synthetic.main.activity_search_container.*

class SearchContainerActivity : AppCompatActivity(), SearchContract.View {

    override lateinit var presenter: SearchContract.Presenter
    override val lifeCycle: LifecycleOwner = this
    private var productModel:ProductModel? = null
    var next: Boolean = true
    var queryString : String = ""
    var catString :String = ""

    private val searchResultFragment = SearchResultFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_container)
        /*setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, SearchFragment(), "searchFrag")
        }.commit()
        SearchPresenter(this)
        presenter.getCategories()

        val et = searchViewQuery.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        et.setTextColor(Color.BLACK)
        et.setHintTextColor(Color.GRAY)
        et.requestFocus()
        searchViewQuery.isIconified = false
        searchViewQuery.setIconifiedByDefault(true)
        searchViewQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.performSearch(query, "1", "", "")
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onError(errorMessage: Int) {

    }

    override fun onDataLoading() {

    }

    override fun onSearchResults(productModel: ProductModel, page: Page) {
        this.productModel = productModel
        next = page.next
        val resultFragment = supportFragmentManager.findFragmentByTag("result")
        if(resultFragment == null){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, searchResultFragment, "result")
                addToBackStack(null)
            }.commit()
        }
        (resultFragment as SearchResultFragment).refreshLayout(productModel.viewModel)
    }

    override fun onCategories(categories: ArrayList<CategoryModel>) {
        val searchFrag = supportFragmentManager.findFragmentByTag("searchFrag") as SearchFragment
        searchFrag.onCategories(categories)
    }
}
