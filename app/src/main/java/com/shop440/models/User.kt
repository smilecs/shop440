package com.shop440.models

import java.io.Serializable


/**
 * Created by SMILECS on 12/27/16.
 */

class User : Serializable {
    var phone = ""
    var image = ""
    var name = ""
    var email = ""
    var passcode = ""
    var password = ""
    var token = ""

    override fun toString(): String {
        return name
    }
}
