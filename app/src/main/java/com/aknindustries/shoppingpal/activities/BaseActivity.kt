package com.aknindustries.shoppingpal.activities

import android.app.Dialog
import android.graphics.LightingColorFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_indicator.*

open class BaseActivity: AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    fun showSnackBar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snack_bar_error))
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.snack_bar_success))
        }
        snackBar.setTextColor(ContextCompat.getColor(this@BaseActivity, R.color.white))
        snackBar.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)
        mProgressDialog.window?.decorView?.background?.colorFilter = LightingColorFilter(
            0xFF000000.toInt(),
            0xFFffffff.toInt(),
        )
        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.progress_indicator)
        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}