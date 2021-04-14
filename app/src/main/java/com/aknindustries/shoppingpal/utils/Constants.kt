package com.aknindustries.shoppingpal.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {

    const val USERS: String = "users"
    const val APP_PREFERENCES = "ShoppingPal"
    const val LOGGED_IN_USERNAME = "logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE = "Male"
    const val FEMALE = "Female"
    const val PHONE = "phone"
    const val GENDER = "gender"

    fun showImageChooser(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

}