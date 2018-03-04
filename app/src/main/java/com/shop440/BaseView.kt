package com.shop440

/**
 * Created by mmumene on 25/08/2017.
 */

interface BaseView<T> {
    var presenter: T
    fun onError(errorMessage: Int)
    fun onDataLoading()
}