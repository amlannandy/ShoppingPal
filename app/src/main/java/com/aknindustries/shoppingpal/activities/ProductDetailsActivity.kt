package com.aknindustries.shoppingpal.activities

import android.annotation.SuppressLint
import android.os.Bundle
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.firebase.FireStoreProductClass
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()

        if (intent.hasExtra(Constants.PRODUCT_ID)) {
            val productId = intent.getStringExtra(Constants.PRODUCT_ID)!!
            fetchProduct(productId)
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_product_details_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun fetchProduct(productId : String) {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreProductClass().fetchProduct(this, productId)
    }

    @SuppressLint("SetTextI18n")
    fun fetchProductSuccess(product : Product) {
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductImage(
            product.imageUrl,
            iv_product_detail_image
        )
        tv_product_details_title.text = product.title
        tv_product_details_price.text = "${Constants.RUPEE_SIGN} ${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_stock_quantity.text = product.quantity.toString()
    }

    fun fetchProductFailure(errorMessage : String) {
        hideProgressDialog()
        showSnackBar(errorMessage, false)
    }

}