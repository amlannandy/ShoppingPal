package com.aknindustries.shoppingpal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.aknindustries.shoppingpal.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context : Context) {

    fun loadUserImage(image: Any, imageView: ImageView) {
        try {
            Glide.with(context).load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

}