package com.shop440.dao.models

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Page(@SerializedName("Prev")
           val prev: Boolean, @SerializedName("PrevVal")
           val prevVal: Int, @SerializedName("Next")
           val next: Boolean, @SerializedName("NextVal")
           val nextVal: Int, @SerializedName("NextURL")
           val nextURL: String, @SerializedName("Pages")
           val pages: List<String>, @SerializedName("Total")
           val total: Int, @SerializedName("Count")
           val count: Int, @SerializedName("Skip")
           val skip: Int) : Serializable
