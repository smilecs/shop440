package com.shop440.utils

import android.app.ProgressDialog
import android.content.Context
import com.shop440.R

/**
 * Created by mmumene on 05/09/2017.
 */
class ProgressDialog{
    companion object{
        @JvmStatic fun progressDialog(context: Context):ProgressDialog{
            val progressDialog = ProgressDialog(context)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage(context.getString(R.string.loading))
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            return progressDialog

        }
    }
}