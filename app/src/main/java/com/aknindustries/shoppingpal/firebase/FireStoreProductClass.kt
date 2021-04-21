package com.aknindustries.shoppingpal.firebase

import android.net.Uri
import androidx.fragment.app.Fragment
import com.aknindustries.shoppingpal.activities.AddProductActivity
import com.aknindustries.shoppingpal.fragments.DashboardFragment
import com.aknindustries.shoppingpal.fragments.ProductsFragment
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FireStoreProductClass {

    private val mFireStoreClass = FirebaseFirestore.getInstance()

    fun fetchProducts(fragment: Fragment) {
        mFireStoreClass.collection(Constants.PRODUCTS).get()
            .addOnSuccessListener { snapshot ->
                val docs = snapshot.documents
                val products = ArrayList<Product>()
                for (doc in docs) {
                    val product = doc.toObject(Product::class.java)
                    product!!.id = doc.id
                    products.add(product)
                }
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.fetchProductsSuccess(products)
                    }
                }
            }.addOnFailureListener { e ->
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.fetchProductsFailure(e.message.toString())
                    }
                }
            }
    }

    fun fetchUserProducts(fragment: Fragment) {
        mFireStoreClass.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, FireStoreAuthClass().getCurrentUserId()).get()
            .addOnSuccessListener { snapshot ->
                val docs = snapshot.documents
                val userProducts = ArrayList<Product>()
                for (doc in docs) {
                    val product = doc.toObject(Product::class.java)
                    product!!.id = doc.id
                    userProducts.add(product)
                }
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.fetchProductsSuccess(userProducts)
                    }
                }
            }.addOnFailureListener { e ->
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.fetchProductsFailure(e.message.toString())
                    }
                }
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

    fun deleteProduct(fragment: Fragment, productId: String) {
        mFireStoreClass.collection(Constants.PRODUCTS).document(productId).delete()
            .addOnSuccessListener {
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.deleteProductSuccess()
                    }
                }
            }.addOnFailureListener {
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.deleteProductFailure()
                    }
                }
            }
    }

}