package com.shop440.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shop440.adapters.viewholders.BaseViewHolder
import com.shop440.adapters.viewholders.TypesFactoryImpl
import com.shop440.adapters.viewmodel.ViewModel

/**
 * Created by mmumene on 22/11/2017.
 */

class NestedFeedAdapter(val viewModel:ViewModel) : RecyclerView.Adapter<BaseViewHolder<ViewModel>>(){
    private val typeFactory = TypesFactoryImpl()
    override fun onBindViewHolder(holder: BaseViewHolder<ViewModel>?, position: Int) {
        holder?.bind(viewModel, position)
    }

    override fun getItemCount() = viewModel.size()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<ViewModel> {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as BaseViewHolder<ViewModel>
    }

    override fun getItemViewType(position: Int) = viewModel.type()
}