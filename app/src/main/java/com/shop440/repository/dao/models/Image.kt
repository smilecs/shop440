package com.shop440.repository.dao.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Image : Serializable {

    @SerializedName("path")
    @Expose
    var path: String? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
    @SerializedName("base64_placeholder")
    @Expose
    var placeholder: String? = null
        get() = field!!.split("data:image/jpeg;base64,".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    @SerializedName("Thumbnail")
    @Expose
    var thumbnail: String? = null

    var url:String? = null

    var name:String? = null

}
