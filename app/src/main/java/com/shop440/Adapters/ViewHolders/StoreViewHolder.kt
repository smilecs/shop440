package com.shop440.Adapters.ViewHolders

import android.view.View
import com.shop440.Adapters.ViewModel.StoreViewModel
import com.shop440.Adapters.ViewModel.ViewModel
import com.shop440.models.Feed

/**
 * Created by mmumene on 21/11/2017.
 */


class StoreViewHolder(view:View): FeedViewHolder<StoreViewModel>(view){
    override fun bind(item: StoreViewModel) {
        title.text = item.title
    }
}