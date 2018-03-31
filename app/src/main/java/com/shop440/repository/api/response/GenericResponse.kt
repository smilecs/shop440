package com.shop440.repository.api.response

import com.google.gson.annotations.SerializedName

/**
 * Created by mmumene on 22/02/2018.
 */
class GenericResponse(@SerializedName("message", alternate = ["Message"]) val message:String)