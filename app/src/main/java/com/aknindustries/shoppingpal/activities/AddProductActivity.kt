package com.aknindustries.shoppingpal.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreProductClass
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()
        icon_btn_add_image.setOnClickListener(this@AddProductActivity)
        btn_add_product.setOnClickListener(this@AddProductActivity)
    }

    private fun validateAddProduct() : Boolean {
        val title = et_product_title.text.toString().trim()
        val description = et_product_description.text.toString().trim()
        val price = et_product_price.text.toString().trim()
        val quantity = et_product_quantity.text.toString().trim()
        return when {
            TextUtils.isEmpty(title) -> {
                showSnackBar(resources.getString(R.string.err_product_title), true)
                return false
            }
            TextUtils.isEmpty(description) -> {
                showSnackBar(resources.getString(R.string.err_product_description), true)
                return false
            }
            TextUtils.isEmpty(price).or(!TextUtils.isDigitsOnly(price)) -> {
                showSnackBar(resources.getString(R.string.err_product_price), true)
                return false
            }
            TextUtils.isEmpty(quantity).or(!TextUtils.isDigitsOnly(quantity)) -> {
                showSnackBar(resources.getString(R.string.err_product_quantity), true)
                return false
            }
            mImageUri == null -> {
                showSnackBar(resources.getString(R.string.err_product_image), true)
                return false
            }
            else -> true
        }
    }

    fun imageUploadSuccess(imageUrl: String) {
        val title = et_product_title.text.toString().trim()
        val description = et_product_description.text.toString().trim()
        val price = et_product_price.text.toString().trim()
        val quantity = et_product_quantity.text.toString().trim()
        val product = Product(
            title = title,
            description = description,
            price = price.toDouble(),
            quantity = quantity.toInt(),
            imageUrl = imageUrl
        )
        FireStoreProductClass().addProduct(this, product)
    }

    fun addProductSuccess() {
        hideProgressDialog()
        showSnackBar(resources.getString(R.string.add_product_success), false)
        finish()
    }

    fun addProductFailure(errorMessage : String) {
        hideProgressDialog()
        showSnackBar(errorMessage, true)
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_add_product_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.icon_btn_add_image -> {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_add_product -> {
                    if (validateAddProduct()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FireStoreProductClass().uploadProductImage(this, mImageUri!!)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                showSnackBar(resources.getString(R.string.read_storage_permission_denied), false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mImageUri = data.data!!
                        GlideLoader(this).loadUserImage(mImageUri!!, product_image)
                    } catch (e : IOException) {
                        e.printStackTrace()
                        showSnackBar(resources.getString(R.string.image_selection_failure), true)
                    }
                }
            }
        }
    }

}