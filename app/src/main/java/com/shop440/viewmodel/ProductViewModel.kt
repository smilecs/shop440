package com.shop440.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.shop440.repository.dao.KategoryDao
import com.shop440.repository.dao.models.CategoryModel
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 01/03/2018.
 */
class ProductViewModel : ViewModel() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getCategories(): LiveData<RealmResults<CategoryModel>> {
        return realm.KategoryDao().getCategories()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}