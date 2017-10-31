package com.shop440.ProductView

import com.shop440.Models.Datum
import com.shop440.R
import com.shop440.Utils.FileCache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by mmumene on 09/09/2017.
 */
class ProductViewPresenter(val productView: ProductViewContract.View, val retrofit: Retrofit) : ProductViewContract.Presenter {

    init {
        productView.presenter = this
    }

    override fun start() {

    }

    override fun loadData(path: String) {
        productView.onDataLoading()
        val data: Call<Datum> = retrofit.create(ApiRequest::class.java).getProduct(path)
        data.enqueue(object : Callback<Datum> {
            override fun onResponse(call: Call<Datum>?, response: Response<Datum>?) {
                if (response!!.isSuccessful) {
                    productView.showProduct(response.body()!!)
                } else {
                    productView.onError(R.string.api_data_load_error)
                }
            }

            override fun onFailure(call: Call<Datum>?, t: Throwable?) {
                productView.onError(R.string.internet_error_message)
            }
        })
    }

    @Throws(MalformedURLException::class)
    override fun downloadImage(imageUrl: String, productName: String, filePath: FileCache) {
        val type = "jpg"
        val file = filePath.getFile(productName, type)
        val url = URL(imageUrl)
        val tm = Thread(Runnable {
            try {
                val ucon = url.openConnection()
                val `is` = ucon.getInputStream()
                val inStream = BufferedInputStream(`is`, 5 * 1024)
                val outStream = FileOutputStream(file)
                val buff = ByteArray(5 * 1024)

                //Read bytes (and store them) until there is nothing more to read(-1)
                var len: Int = 0
                while (len != -1) {
                    len = inStream.read(buff)
                    outStream.write(buff, 0, len)
                }
                outStream.flush()
                outStream.close()
                inStream.close()
                productView.imageDownloaded(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        tm.start()

    }
}