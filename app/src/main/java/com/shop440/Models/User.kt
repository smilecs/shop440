package com.shop440.Models

import java.io.Serializable

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

    override fun toString(): String {
        return name
    }
}
