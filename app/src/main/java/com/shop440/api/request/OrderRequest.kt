package com.shop440.api.request

import com.google.gson.annotations.SerializedName
import com.shop440.checkout.models.Order

/**
 * Created by mmumene on 18/03/2018.
 */

class OrderRequest(val order: Order) {

    val phone = order.phone
    val name = order.name
    val email = order.email
    @SerializedName(value = "postalcode")
    val postalCode = order.postalCode
    val city = order.city
    @SerializedName(value = "address1")
    val address = order.address
    val company = order.company
    val province = order.province
    val total = order.total
    val shopOrders = order.shopOrders.toList()

}