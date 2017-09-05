package com.shop440.Models

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by SMILECS on 12/27/16.
 */

class User : Serializable {
    var phone: String = ""
    var image: String = ""
    var name: String = ""
    var email: String = ""
    var passcode: String = ""
    var likes: String = ""
    var stores: String = ""
    var purchases: String = ""

    @SerializedName("User")
    @Expose
    var user:User_? = null

    @SerializedName("Message")
    @Expose
    var message = ""

    @SerializedName("Token")
    @Expose
    var token =  ""

    override fun toString(): String {
        return name
    }
}
