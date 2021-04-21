package com.aknindustries.shoppingpal.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aknindustries.shoppingpal.R
import com.aknindustries.shoppingpal.models.Product
import com.aknindustries.shoppingpal.utils.Constants
import com.aknindustries.shoppingpal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class UserProductsAdaptor (
    private val context: Context,
    private val products : ArrayList<Product>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_list_layout, parent, false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductImage(product.imageUrl, holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = product.title
            holder.itemView.tv_item_price.text = "${Constants.RUPEE_SIGN} ${product.price}"
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}