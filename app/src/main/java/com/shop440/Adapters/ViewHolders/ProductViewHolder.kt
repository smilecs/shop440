package com.shop440.Adapters.ViewHolders

import android.view.View
import com.shop440.Adapters.ViewModel.ProductViewModel

/**
 * Created by mmumene on 21/11/2017.
 */

class ProductViewHolder(view: View):FeedViewHolder<ProductViewModel>(view){
    override fun bind(item: ProductViewModel) {
        title.text = item.title
    }
}