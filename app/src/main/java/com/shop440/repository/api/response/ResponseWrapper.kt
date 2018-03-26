package com.shop440.repository.api.response

/**
 * Created by mmumene on 18/03/2018.
 */
class ResponseWrapper<T>{
    var error:Boolean = true
    var resp : T? = null
}