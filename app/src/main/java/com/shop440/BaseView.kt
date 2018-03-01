package com.shop440

import android.arch.lifecycle.LifecycleOwner

/**
 * Created by mmumene on 25/08/2017.
 */

interface BaseView<T> {
    var presenter: T
    fun onError(errorMessage: Int)
    fun onDataLoading()
}