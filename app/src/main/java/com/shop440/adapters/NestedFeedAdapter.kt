package com.shop440.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.shop440.BaseViewHolder
import com.shop440.typefactory.TypesFactoryImpl
import com.shop440.features.navigation.home.adaptermodel.AdapterModel

/**
 * Created by mmumene on 22/11/2017.
 */

class NestedFeedAdapter(val adapterModel: AdapterModel, val requestManager: RequestManager?) : RecyclerView.Adapter<BaseViewHolder<AdapterModel>>(){
    private val typeFactory = TypesFactoryImpl()
    override fun onBindViewHolder(holder: BaseViewHolder<AdapterModel>?, position: Int) {
        holder?.bind(adapterModel, position)
    }

    override fun getItemCount() = adapterModel.size()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<AdapterModel> {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view, requestManager) as BaseViewHolder<AdapterModel>
    }

    override fun getItemViewType(position: Int) = adapterModel.type()
}