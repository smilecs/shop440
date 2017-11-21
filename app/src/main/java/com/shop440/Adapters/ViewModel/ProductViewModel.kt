package com.shop440.Adapters.ViewModel

import com.shop440.models.Feed

/**
 * Created by mmumene on 21/11/2017.
 */

class ProductViewModel(viewTitle:String, val viewModel: Feed.ProductFeed): ViewModel(viewTitle){

    override fun type(types: TypeFactory): Int{
        return types.type(viewModel)
    }
}