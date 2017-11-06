package com.shop440.api

/**
 * Created by SMILECS on 12/27/16.
 */

object Urls {
    const val BASE_URL = "http://192.168.1.59:8080"
    const val NEW_USER = "/api/sign_up"
    const val PASSCODE = "/api/get_passcode"
    const val UPDATE_USER = "/api/update_name_and_email"
    //const val BASE_URL = "http://192.168.1.59:8080"
    const val ME = "/api/me"
    const val LOGIN = "/api/login/phone"
    const val MyStores = "/api/me/stores"
    const val NEWSTORE = "/api/new_store"
    const val GETPRODUCTS = "/api/products"
    const val GETPRODUCT = "/api/products/"
    const val GETSTOREPRODUCTS = "/api/stores/"
    const val GETCATEGORIES = "/api/categories"
    const val ADDPRODUCT = "/api/me/stores/"
    const val SINGLESTORE = "/api/stores/"
    const val OTP = "/api/users/verifyphone"
    const val TOKEN = "tokens"
}
