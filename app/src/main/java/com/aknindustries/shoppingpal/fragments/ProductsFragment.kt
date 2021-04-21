package com.aknindustries.shoppingpal.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.activities.AddProductActivity
import com.aknindustries.shoppingpal.activities.ProductDetailsActivity
import com.aknindustries.shoppingpal.adaptors.UserProductsAdaptor
import com.aknindustries.shoppingpal.firebase.FireStoreProductClass
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_product -> {
                Log.d("Trigger", "Check")
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        fetchProducts()
    }

    private fun fetchProducts() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreProductClass().fetchUserProducts(this)
    }

    fun fetchProductsSuccess(products : ArrayList<Product>) {
        hideProgressDialog()
        if (products.isNotEmpty()) {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.INVISIBLE
            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adaptorProducts = UserProductsAdaptor(requireActivity(), products, this)
            rv_my_product_items.adapter = adaptorProducts
        } else {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
    }

    fun fetchProductsFailure(message : String) {
        hideProgressDialog()
        Log.d("Fetch Products Error", message)
    }

    fun deleteProductConfirmation(productId: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_product_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_product_dialog))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        // Yes
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            deleteProduct(productId)
            dialogInterface.dismiss()
        }
        // No
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun deleteProduct(productId : String) {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreProductClass().deleteProduct(this, productId)
    }

    fun deleteProductSuccess() {
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.delete_product_success),
            Toast.LENGTH_SHORT,
        ).show()
        fetchProducts()
    }

    fun deleteProductFailure() {
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.delete_product_failure),
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun goToProductDetails(productId : String) {
        val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
        intent.putExtra(Constants.PRODUCT_ID, productId)
        startActivity(intent)
    }
}