package com.shop440.Adapters.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.shop440.Adapters.ViewModel.ViewModel
import com.shop440.R
import com.shop440.widgets.FontTextView

/**
 * Created by mmumene on 21/11/2017.
 */
abstract class FeedViewHolder<in T>(view: View) : BaseViewHolder<T>(view) {

    val title: FontTextView by lazy {
        view.findViewById(R.id.nestedTitle) as FontTextView
    }
    val recyclerView: RecyclerView by lazy {
        view.findViewById(R.id.nestedRecycler) as RecyclerView
    }
}