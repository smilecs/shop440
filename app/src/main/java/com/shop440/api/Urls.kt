package com.shop440.api

/**
 * Created by SMILECS on 12/27/16.
 */

object Urls {
    const val BASE_URL = "http://192.168.8.100:8080"
    const val NEW_USER = "/api/users/signup"
    //const val BASE_URL = "http://192.168.8.104:8080"
    //const val BASE_URL = "https://shop440.com"
    const val LOGIN = "/api/users/login"
    const val CHECKPHONE = "/api/users/checkphonenumber"
    const val HOME_PAGE_SECTION = "/api/home/sections"
    const val OTP = "/api/users/verifyphone"
    const val TOKEN = "tokens"
    const val CATEGORY = "/api/categories"
    const val GET_PRODUCT = "/api/{shopid}/{productslug}"
    const val NEW_ORDER = "/api/new"
    const val TINYFILES = "https://tinyfiles.past3dev.com/resize?width=200&height=0&type=jpeg&nocrop=true&url="
}
