package com.aknindustries.shoppingpal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.activities.ProductDetailsActivity
import com.aknindustries.shoppingpal.activities.SettingsActivity
import com.aknindustries.shoppingpal.adaptors.DashboardItemsAdaptor
import com.aknindustries.shoppingpal.firebase.FireStoreProductClass
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
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
        FireStoreProductClass().fetchProducts(this)
    }

    fun fetchProductsSuccess(products : ArrayList<Product>) {
        hideProgressDialog()
        if (products.isNotEmpty()) {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.INVISIBLE
            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)
            val adaptorProducts = DashboardItemsAdaptor(requireActivity(), products, this)
            rv_dashboard_items.adapter = adaptorProducts
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    fun fetchProductsFailure(message : String) {
        hideProgressDialog()
        Log.d("Fetch Products Error", message)
    }

    fun goToProductDetails(product: Product) {
        val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        intent.putExtra(Constants.PRODUCT_USER_ID, product.userId)
        startActivity(intent)
    }
}