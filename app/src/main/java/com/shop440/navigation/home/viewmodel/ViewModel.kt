package com.shop440.navigation.home.viewmodel

/**
 * Created by mmumene on 19/11/2017.
 */
abstract class ViewModel(var title:String){
    abstract fun type() :Int
    abstract fun size():Int
}