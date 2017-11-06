package com.shop440.productview

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.models.Datum
import com.shop440.utils.FileCache
import java.io.File

/**
 * Created by mmumene on 09/09/2017.
 */

interface ProductViewContract {

    interface View : BaseView<Presenter> {
        fun showProduct(product: Datum)
        fun imageDownloaded(filePath: File)
    }

    interface Presenter : BasePresenter {
        fun loadData(path: String)
        fun downloadImage(imageUrl: String, productName: String, filePath: FileCache)
    }
}