package com.shop440.Adapters.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by mmumene on 19/11/2017.
 */
 abstract class ViewHolder<in T> (view: View) : RecyclerView.ViewHolder(view){
    abstract fun bind(item: T)
}
