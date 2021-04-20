package com.aknindustries.shoppingpal.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val APP_PREFERENCES = "ShoppingPal"
    const val LOGGED_IN_USERNAME = "logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE = "Male"
    const val FEMALE = "Female"

    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val EMAIL = "email"
    const val PHONE = "phone"
    const val GENDER = "gender"
    const val IMAGE_URL = "imageUrl"
    const val PROFILE_COMPLETED = "profileCompleted"

    const val PROFILE_PICTURE_FOLDER = "profile_pictures"
    const val USER_PROFILE_IMAGE = "USER_PROFILE_IMAGE"

    const val PRODUCTS = "products"
    const val PRODUCT_IMAGES_FOLDER = "product_images"
    const val PRODUCT_IMAGE = "PRODUCT_IMAGE"

    fun showImageChooser(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri))
    }

}