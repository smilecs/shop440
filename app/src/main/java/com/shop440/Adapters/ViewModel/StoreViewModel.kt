package com.shop440.Adapters.ViewModel

import com.shop440.R
import com.shop440.models.Feed
import com.shop440.models.StoreFeed
import com.shop440.models.StoreModel

/**
 * Created by mmumene on 21/11/2017.
 */

class StoreViewModel(viewTitle:String, val viewModel: List<StoreFeed>): ViewModel(viewTitle){

    override fun type(types: TypeFactory): Int{
        return types.type(viewModel)
    }
}