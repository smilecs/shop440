package com.shop440.adapters

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.navigation.home.viewmodel.ViewModel
import com.shop440.view.ItemDecorator
import com.shop440.widgets.FontTextView

/**
 * Created by mmumene on 19/11/2017.
 */
class TopFeedAdapter(val viewModel: List<ViewModel>, val context: Context, val isLinear: Boolean) : RecyclerView.Adapter<TopFeedAdapter.ViewHolder.BaseViewHolder>() {
    constructor(viewModel: List<ViewModel>, context: Context) : this(viewModel, context, false)

    private val viewPool: RecyclerView.RecycledViewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    override fun onBindViewHolder(holder: ViewHolder.BaseViewHolder?, position: Int) {
        val model = viewModel[position]
        holder?.onBind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder.BaseViewHolder{
        val viewHolder = if(isLinear){
            ViewHolder.LinearViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.linear_recycler, parent, false))
        }else{
            ViewHolder.GridViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.nested_reycler, parent, false))
        }
        viewHolder.recyclerView.recycledViewPool = viewPool
        //LinearSnapHelper().attachToRecyclerView(viewHolder.recyclerView)
        return viewHolder
    }

    override fun getItemCount() = viewModel.size

    object ViewHolder {

        abstract class BaseViewHolder(val view:View) : RecyclerView.ViewHolder(view){
            val recyclerView: RecyclerView by lazy {
                view.findViewById<RecyclerView>(R.id.nestedRecycler)
            }

            val title: FontTextView by lazy {
                view.findViewById<FontTextView>(R.id.nestedTitle)
            }

            abstract fun onBind(viewModel:ViewModel)
        }

        class GridViewHolder(val views: View) : BaseViewHolder(views){
            override fun onBind(viewModel: ViewModel) {
                title.text = viewModel.title
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                    adapter = NestedFeedAdapter(viewModel)
                }
            }
        }

        class LinearViewHolder(views:View) : BaseViewHolder(views){
            override fun onBind(viewModel: ViewModel) {
                title.text = viewModel.title
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(ItemDecorator(context, LinearLayoutManager.VERTICAL))
                    adapter = NestedFeedAdapter(viewModel)
                }
            }
        }
    }
}