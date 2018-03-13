package com.shop440.dao.models

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(val lat:String, val lon:String) : Serializable
