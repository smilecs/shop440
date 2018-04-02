package com.shop440.features.search

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.shop440.R
import com.shop440.repository.dao.models.CategoryModel
import com.shop440.repository.dao.models.Page
import com.shop440.features.navigation.home.adaptermodel.ProductModel
import kotlinx.android.synthetic.main.activity_search_container.*
import java.lang.ref.WeakReference
import java.util.*


class SearchContainerActivity : AppCompatActivity(), SearchContract.View {

    override lateinit var presenter: SearchContract.Presenter
    override val lifeCycle: LifecycleOwner = this
    private var productModel: ProductModel? = null
    var next: Boolean = true
    var queryString: String = ""
    var catString: String = ""
    private val resultFragment = SearchResultFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_container)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, SearchFragment(), "searchFrag")
        }.commit()
        val reference = WeakReference<SearchContract.View>(this)
        SearchPresenter(reference.get())
        presenter.getCategories()

        val et = searchViewQuery.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        et.setTextColor(Color.BLACK)
        et.setHintTextColor(Color.GRAY)
        et.requestFocus()
        searchViewQuery.isIconified = false
        searchViewQuery.setIconifiedByDefault(false)
        searchViewQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                startSearch(query, "1", "", "")
                resultFragment.list.clear()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryString = ""
                return false
            }
        })
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
       toggleProgressBar()
    }

    override fun onSearchResults(productModel: ProductModel, page: Page) {
        container.visibility = View.VISIBLE
        this.productModel = productModel
        next = page.next
        resultFragment.refreshLayout(productModel.viewModel)
    }

    override fun onCategories(categories: ArrayList<CategoryModel>) {
        container.visibility = View.VISIBLE
        val sortCategories = mutableListOf<CategoryModel>()
        val compare = Comparator { t: CategoryModel, y: CategoryModel ->
            t.catName.compareTo(y.catName)
        }
        sortCategories.apply {
            addAll(categories)
            sortWith(compare)
        }
        val searchFrag = supportFragmentManager.findFragmentByTag("searchFrag") as SearchFragment
        searchFrag.onCategories(sortCategories as ArrayList<CategoryModel>)
    }

    private fun isInForeGround() = resultFragment.isVisible

    private fun switchFragment() {
        if (!isInForeGround()) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, resultFragment, "result")
            }.commit()
        }
    }

    fun startSearch(query: String, page: String, cat: String, tag: String) {
        switchFragment()
        presenter.performSearch(query, page, cat, tag)
    }

    private fun toggleProgressBar() {
        progressBar.visibility = if (progressBar.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
