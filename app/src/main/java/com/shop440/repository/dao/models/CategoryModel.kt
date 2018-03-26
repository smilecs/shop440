package com.shop440.repository.dao.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * Created by SMILECS on 1/24/17.
 */

open class CategoryModel : RealmObject(){
    @SerializedName("Name") var catName: String = ""
    @SerializedName("Slug") var slug: String = ""
}