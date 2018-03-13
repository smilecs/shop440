package com.shop440.search

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.shop440.R
import com.shop440.dao.models.CategoryModel
import com.shop440.dao.models.Page
import com.shop440.navigation.home.adaptermodel.ProductModel
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
    private val resultFragment: SearchResultFragment by lazy {
        supportFragmentManager.findFragmentByTag("result") as SearchResultFragment
    }

    private val searchResultFragment = SearchResultFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_container)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, searchResultFragment, "result")
            replace(R.id.container, SearchFragment(), "searchFrag")
            addToBackStack(null)
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
                presenter.performSearch(query, "1", "", "")
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

    }

    override fun onDataLoading() {

    }

    override fun onSearchResults(productModel: ProductModel, page: Page) {
        this.productModel = productModel
        next = page.next
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, resultFragment, "result")
        }.commit()

        resultFragment.refreshLayout(productModel.viewModel)
    }

    override fun onCategories(categories: ArrayList<CategoryModel>) {
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
}
