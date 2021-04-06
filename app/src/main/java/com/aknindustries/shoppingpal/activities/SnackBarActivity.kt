package com.aknindustries.shoppingpal.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.google.android.material.snackbar.Snackbar

open class SnackBarActivity: AppCompatActivity() {

    fun showSnackBar(message: String, isError: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        if (isError) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@SnackBarActivity, R.color.snack_bar_error))
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@SnackBarActivity, R.color.snack_bar_success))
        }
        snackBar.setTextColor(ContextCompat.getColor(this@SnackBarActivity, R.color.white))
        snackBar.show()
    }

}