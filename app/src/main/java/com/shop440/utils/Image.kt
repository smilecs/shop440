package com.shop440.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Base64
import android.util.Log

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Image entity
 */
class Image(private val mContext: Context, private val mUri: Uri) {

    //Retrieve the bitmap from its Uri (in sample size calculated)
    val bitmapFromUri: Bitmap?
        get() {
            Log.v(TAG, "Decode image to bitmap")
            return decodeSampledBitmapFromUrl(450, 450)
        }

    /*
	     * Get the column indexes of the data in the Cursor,
	     * move to the first row in the Cursor, get the data,
	     * and display it.
	     */ val fileName: String
        get() {
            val cursor = mContext.contentResolver.query(mUri, null, null, null, null)
            val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val name = cursor.getString(nameIndex)
            cursor.close()

            return name
        }

    val fileSize: Long
        get() {
            val cursor = mContext.contentResolver.query(mUri, null, null, null, null)

            val sizeIndex = cursor!!.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val size = cursor.getLong(sizeIndex)
            cursor.close()

            return size
        }

    //Calculate the size of the image to be loaded (prevent memory leak due to large bitmap loading)
    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    //Load the bitmap in the size calculated
    fun decodeSampledBitmapFromUrl(reqWidth: Int, reqHeight: Int): Bitmap? {
        var input: InputStream?
        try {
            input = mContext.contentResolver.openInputStream(mUri)

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(input, null, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight)
            input!!.close()

            // Decode bitmap with inSampleSize set
            input = mContext.contentResolver.openInputStream(mUri)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(input, null, options)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "Failed to decode the bitmap with the size selected")
        }

        return null
    }

    companion object {
        val TAG = "Image"

        fun roundedBitmapDrawable(context: Context, bitmap: Bitmap): RoundedBitmapDrawable {
            val dr = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
            dr.cornerRadius = Math.max(bitmap.width, bitmap.height) / 2.0f
            return dr
        }

        fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
            Log.v(TAG, "Convert image to byte array")
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            try {
                baos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return b
        }

        fun base64String(bitmap: Bitmap?): String {
            return if(bitmap != null){
                Base64.encodeToString(bitmapToByteArray(bitmap), Base64.DEFAULT)
            } else{
                ""
            }
        }
    }
}
