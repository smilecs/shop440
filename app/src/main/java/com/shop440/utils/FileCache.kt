package com.shop440.utils

import android.content.Context

import java.io.File
import java.net.URLEncoder

/**
 * Created by SMILECS on 8/10/16.
 */
class FileCache(context: Context) {

    private var cacheDir: File? = null

    init {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
            cacheDir = File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "Shop440")
        } else {
            cacheDir = File(context.cacheDir, "Shop440")
        }
        if (!cacheDir!!.exists()) {
            cacheDir!!.mkdirs()
        }
    }

    fun getFile(url: String, type: String): File {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        val filename = URLEncoder.encode(url) + "." + type
        // String filename = URLEncoder.encode(url);
        return File(cacheDir, filename)

    }

    fun directory(): String {
        return cacheDir!!.absolutePath
    }

    fun clear() {
        val files = cacheDir!!.listFiles() ?: return
        for (f in files)
            f.delete()
    }

}
