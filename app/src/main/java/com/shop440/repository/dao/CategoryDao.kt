package com.shop440.repository.dao

import android.arch.lifecycle.LiveData
import com.shop440.repository.dao.models.CategoryModel
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 01/03/2018.
 */

class CategoryDao(val realm: Realm){

    fun getCategories() : LiveData<RealmResults<CategoryModel>>{
        return realm.where(CategoryModel::class.java).findAllAsync().asLiveData()
    }

}
