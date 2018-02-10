package com.shop440.checkout.models

/**
 * Created by mmumene on 04/02/2018.
 */
data class ItemForKart(val itemName: String, val shopName: String, val slug: String, val id: String, val shopSlug: String) {
    var quantity: Int = 0
    var amount: Double = 0.0
    var item: Item? = null
}