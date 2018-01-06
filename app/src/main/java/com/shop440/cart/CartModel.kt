package com.shop440.cart

import com.google.gson.annotations.SerializedName

/**
 * Created by SMILECS on 1/6/18.
 */

object CartModel {
    data class ItemClass(@SerializedName(value = "total_price") val totalPrice: Double,
                         @SerializedName(value = "slug") val slug: String)

    data class ShopShipItem(@SerializedName(value = "shop_name") val shopName: String,
                            @SerializedName(value = "shop_id") val shopId: String,
                            @SerializedName(value = "shipping") val shipping: Double,
                            @SerializedName(value = "items_cost") val itemCost: Double,
                            @SerializedName(value = "total") val total: Double)


}


/*{
    "cart": [
    {
        "items": [
        { "total_price": 982, "slug": "cats-and-dogs-22-3-0-xxx-2-uedor" },
        { "total_price": 982, "slug": "cats-and-dogs-22-3-1-xxx-3-dvt15" }
        ],
        "shop_name": "Past3 Studio",
        "shop_id": "past3",
        "shipping": 1000,
        "items_cost": 1964,
        "total": 26676
    }
    ],
    "total_price": 26676,
    "delivery_type": "Shop440HomeDelivery",
    "name": "Anthony Alaribe",
    "company": "Oddjobz",
    "address1": "qc 28 unical staff quaters",
    "city": "Calabar",
    "province": "Cross River State",
    "postalcode": "540242",
    "phone": "8161115653",
    "email": "anthonyalaribe@gmail.com"
}*/