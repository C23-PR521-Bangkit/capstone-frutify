package com.example.frutify.ui.dashboard.home.seller

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frutify.R
import com.example.frutify.data.model.ProductItem
import com.example.frutify.ui.dashboard.edit.EditActivity
import com.example.frutify.utils.Helper

class HomeSellerAdapter : RecyclerView.Adapter<HomeSellerAdapter.ListViewHolder>() {
    private val productList = mutableListOf<ProductItem>()
    private lateinit var listener: OnProductClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_row_product, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun submitList(products: List<ProductItem>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_item_name)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_item_cost)
        private val btnEdit: TextView = itemView.findViewById(R.id.tvSelengkapnya)
        private val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)

        fun bind(product: ProductItem) {
            tvProductName.text = product.PRODUCTNAME
            tvProductPrice.text = product.PRODUCTPRICE.toString()

            val imageUrl = Helper.BASE_URL + product.PRODUCTFILEPATH

            Glide.with(itemView)
                .load(imageUrl) // Error image if unable to load
                .into(imgProduct)
            // Implementasikan logika lainnya sesuai kebutuhan Anda

            itemView.setOnClickListener {
                listener.onProductClick(product)
            }
            btnDelete.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EditActivity::class.java)
                // Kirim data produk yang ingin diedit ke EditActivity
                intent.putExtra("product", product)
                intent.putExtra("from_btn_delete", true)
                context.startActivity(intent)
            }
        }
    }

    fun setOnProductClickListener(listener: OnProductClickListener) {
        this.listener = listener
    }

    interface OnProductClickListener {
        fun onProductClick(product: ProductItem)
    }
}