package com.shop440.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.adapters.viewmodel.ViewModel
import com.shop440.R
import com.shop440.widgets.FontTextView

/**
 * Created by mmumene on 19/11/2017.
 */
class TopFeedAdapter(val viewModel: List<ViewModel>, val context: Context) : RecyclerView.Adapter<TopFeedAdapter.ViewHolder.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder.ViewHolder?, position: Int) {
        val model = viewModel[position]
        holder?.title?.text = model.title
        holder?.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NestedFeedAdapter(model)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.nested_reycler, parent, false)
        return ViewHolder.ViewHolder(view)
    }

    override fun getItemCount() = viewModel.size

    object ViewHolder {
        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            lateinit var recyclerView:RecyclerView
            init {
                recyclerView = view.findViewById<RecyclerView>(R.id.nestedRecycler)
                LinearSnapHelper().attachToRecyclerView(recyclerView)
            }
            val title: FontTextView by lazy {
                view.findViewById<FontTextView>(R.id.nestedTitle)
            }
        }
    }
}