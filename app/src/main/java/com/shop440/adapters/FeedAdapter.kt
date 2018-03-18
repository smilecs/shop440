package com.shop440.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.shop440.R
import com.shop440.checkout.models.SummaryAdapterModel
import com.shop440.navigation.home.adaptermodel.AdapterModel
import com.shop440.products.ProductsActivity
import com.shop440.utils.Metrics
import com.shop440.view.ItemDecorator
import com.shop440.widgets.FontTextView
import kotlinx.android.synthetic.main.linear_recycler.view.*
import kotlinx.android.synthetic.main.nested_reycler.view.*

/**
 * Created by mmumene on 19/11/2017.
 */
class TopFeedAdapter(private val adapterModel: List<AdapterModel>,
                     val context: Context,
                     val isLinear: Boolean) : RecyclerView.Adapter<TopFeedAdapter.ViewHolder.BaseViewHolder>() {
    constructor(adapterModel: List<AdapterModel>, context: Context) : this(adapterModel, context, false)

    private val requestManager by lazy {
        Glide.with(context)
    }

    private val viewPool: RecyclerView.RecycledViewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    override fun onBindViewHolder(holder: ViewHolder.BaseViewHolder?, position: Int) {
        val model = adapterModel[position]
        holder?.onBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder.BaseViewHolder {
        val viewHolder = if (isLinear) {
            ViewHolder.LinearViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.linear_recycler, parent, false), requestManager)
        } else {
            ViewHolder.GridViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.nested_reycler, parent, false), requestManager)
        }
        viewHolder.recyclerView.recycledViewPool = viewPool
        return viewHolder
    }

    override fun getItemCount() = adapterModel.size

    object ViewHolder {

        abstract class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val recyclerView: RecyclerView by lazy {
                view.findViewById<RecyclerView>(R.id.nestedRecycler)
            }

            val title: FontTextView by lazy {
                view.findViewById<FontTextView>(R.id.nestedTitle)
            }

            abstract fun onBind(adapterModel: AdapterModel)
        }

        class GridViewHolder(views: View, private val requestManager: RequestManager) : BaseViewHolder(views) {
            init {
                view.seeMoreButton.setOnClickListener({ _ ->
                    val intent = Intent(view.context, ProductsActivity::class.java)
                    intent.putExtra("data", view.tag as String )
                    view.context.startActivity(intent)
                })
            }

            override fun onBind(adapterModel: AdapterModel) {
                title.text = adapterModel.title
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                    adapter = NestedFeedAdapter(adapterModel, requestManager)
                }
                view.tag = adapterModel.slug
            }
        }

        class LinearViewHolder(views: View, private val requestManager: RequestManager) : BaseViewHolder(views) {
            val subText: FontTextView by lazy {
                view.subTotal
            }

            override fun onBind(adapterModel: AdapterModel) {
                title.text = adapterModel.title
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(ItemDecorator(context, LinearLayoutManager.VERTICAL))
                    adapter = NestedFeedAdapter(adapterModel, requestManager)
                }
                if (adapterModel is SummaryAdapterModel) {
                    val sub = adapterModel.item.run {
                        var subAmount = 0.0
                        this.forEach { data -> subAmount += data.amount }
                        subAmount
                    }
                    subText.text = Metrics.getDisplayPriceWithCurrency(view.context, sub)
                }
            }
        }
    }
}