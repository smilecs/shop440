package com.shop440.kart

/**
 * Created by mmumene on 04/02/2018.
 */
data class ItemForKart(val itemName: String, val shopName: String, val slug: String, val amount: Double, val id: String, val shopSlug: String) {
    var quantity: Int = 0
    var item: Item? = null
}