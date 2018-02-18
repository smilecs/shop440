package com.shop440.dao.models

import io.realm.RealmObject

/**
 * Created by mmumene on 18/02/2018.
 */
class UserAdress :RealmObject(){
    var name:String = ""
    var email:String = ""
    var phone:String = ""
    var address:String = ""
    var city:String = ""
}