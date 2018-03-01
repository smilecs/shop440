package com.shop440.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.shop440.dao.CategoryDao
import com.shop440.dao.models.CategoryModel
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 01/03/2018.
 */
class ProductViewModel : ViewModel(){

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getCategories() : LiveData<RealmResults<CategoryModel>>{
        return realm.CategoryDao().getCategories()
    }

}