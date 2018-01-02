package com.shop440.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.io.Serializable

/**
 * Created by SMILECS on 1/24/17.
 */

open class CategoryModel : RealmObject(){
    @SerializedName("Name") var catName: String = ""
    @SerializedName("Slug") var slug: String = ""
}