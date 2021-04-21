package com.aknindustries.shoppingpal.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.fragments.DashboardFragment
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

class DashboardItemsAdaptor(
    private val context: Context,
    private val products: ArrayList<Product>,
    private val fragment: DashboardFragment,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductImage(product.imageUrl, holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_title.text = product.title
            holder.itemView.tv_dashboard_item_price.text = "${Constants.RUPEE_SIGN} ${product.price}"
            holder.itemView.dashboard_item_card.setOnClickListener {
                fragment.goToProductDetails(product.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}