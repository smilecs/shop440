package com.shop440.features.products

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.shop440.R
import com.shop440.repository.dao.models.Page
import com.shop440.repository.dao.models.Product
import com.shop440.features.navigation.home.adaptermodel.ProductModel
import com.shop440.features.search.SearchResultAdapter
import com.shop440.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.searchresult.*

/**
 * A placeholder fragment containing a simple view.
 */
class ProductsActivityFragment : Fragment(), ProductsContract.View {

    var next: Boolean = true
    var total = 1
    val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }
    val slug :String by lazy {
        activity.intent.getSerializableExtra("data") as String
    }

    val list = mutableListOf<Product>()
    val adapter: SearchResultAdapter by lazy {
        SearchResultAdapter(Glide.with(context), list)
    }

    override lateinit var presenter: ProductsContract.Presenter

    override val lifecycleOwner: LifecycleOwner = this

    override val viewModel: ViewModel by lazy {
        ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ProductsPresenter(this)
        return inflater.inflate(R.layout.searchresult, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
        presenter.start()
        if(savedInstanceState == null){
            presenter.loadProducts("", "", slug, "")
            presenter.resolveCategories(slug)
        }
        recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = this@ProductsActivityFragment.adapter
            setHasFixedSize(true)

        }.addOnScrollListener(object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (next && total > page) {
                    presenter.loadProducts("", page.toString(), slug, "")
                }
            }
        })
    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        swipeContainer.isRefreshing  = !swipeContainer.isRefreshing
    }

    override fun onCategoryResolved(category: String) {
        ((activity as ProductsActivity).supportActionBar)?.title = category
    }

    override fun onProductsLoaded(productModel: ProductModel, page: Page) {
        list.addAll(productModel.viewModel)
        next = page.next
        total = page.total
        adapter.notifyDataSetChanged()
    }
}
