package com.aknindustries.shoppingpal.firebase

import android.net.Uri
import com.aknindustries.shoppingpal.activities.AddProductActivity
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FireStoreProductClass {

    private val mFireStoreClass = FirebaseFirestore.getInstance()

    fun fetchAllProducts() {
        mFireStoreClass.collection(Constants.PRODUCTS).get()
            .addOnSuccessListener { snapshot ->
                val docs = snapshot.documents
               
            }
    }

    fun addProduct(activity: AddProductActivity, product: Product) {
        product.userId = FireStoreAuthClass().getCurrentUserId()!!
        product.userName = FireStoreAuthClass().getLocalUserName(activity)
        mFireStoreClass.collection(Constants.PRODUCTS).document()
            .set(product, SetOptions.merge()).addOnSuccessListener {
                activity.addProductSuccess()
            }.addOnFailureListener { e ->
                activity.addProductFailure(e.message.toString())
            }
    }

    fun uploadProductImage(activity: AddProductActivity, imageUri: Uri) {
        val sRef = FirebaseStorage.getInstance().reference.child(
            Constants.PRODUCT_IMAGES_FOLDER +  "/" + FireStoreAuthClass().getCurrentUserId() + "/" +
                    Constants.PRODUCT_IMAGE + System.currentTimeMillis() + Constants.getFileExtension(activity, imageUri))
        sRef.putFile(imageUri).addOnSuccessListener { taskScreenshot ->
            taskScreenshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { imageUri ->
                    activity.imageUploadSuccess(imageUri.toString())
                }
            }.addOnFailureListener { e ->
                activity.addProductFailure(e.message.toString())
            }
    }

}