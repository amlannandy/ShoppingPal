package com.aknindustries.shoppingpal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product (
    val id : String = "",
    var userId : String = "",
    var userName : String = "",
    val title : String = "",
    val description : String = "",
    val price : Double = 0.0,
    val quantity : Int = 0,
    val imageUrl : String = "",
) : Parcelable