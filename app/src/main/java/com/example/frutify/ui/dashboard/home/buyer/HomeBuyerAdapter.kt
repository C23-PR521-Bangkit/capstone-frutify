package com.example.frutify.ui.dashboard.home.buyer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frutify.R
import com.example.frutify.data.model.ProductItem
import com.example.frutify.data.model.ProductItemBuyer
import com.example.frutify.ui.dashboard.home.seller.HomeSellerAdapter
import com.example.frutify.utils.Helper
import org.w3c.dom.Text

class HomeBuyerAdapter : RecyclerView.Adapter<HomeBuyerAdapter.ListViewHolder>() {
    private val productList = mutableListOf<ProductItemBuyer>()
    private var listener: OnProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_row_product_2, parent, false)
        return ListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    fun submitList(products: List<ProductItemBuyer>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView = itemView.findViewById(R.id.ivImg)
        private val tvProductName: TextView = itemView.findViewById(R.id.tvName)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvSeller: TextView = itemView.findViewById(R.id.tvSeller)
        private val quality: TextView = itemView.findViewById(R.id.btn_quality)
        private val fruitType: TextView = itemView.findViewById(R.id.tv_fruit_type)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]
                    listener?.onProductClick(product)
                }
            }
        }

        fun bind(product: ProductItemBuyer) {
            val imageUrl = "https://220d-2404-8000-1039-1102-3dc4-50ff-6ea8-5664.ngrok-free.app/" + product.PRODUCTFILEPATH

            Glide.with(itemView)
                .load(imageUrl)
                .into(imgProduct)

            tvProductName.text = product.PRODUCTNAME
            tvProductPrice.text = product.PRODUCTPRICE.toString()
            tvSeller.text = product.USERFULLNAME
            quality.text = product.PRODUCTQUALITY
            fruitType.text = getFruitTypeName(product.FRUITID!!)
        }
    }

    fun setOnProductClickListener(listener: OnProductClickListener) {
        this.listener = listener
    }

    interface OnProductClickListener {
        fun onProductClick(product: ProductItemBuyer)
    }

    private fun getFruitTypeName(fruitId: Int): String {
        return when (fruitId) {
            1 -> "Apel"
            2 -> "Pisang"
            3 -> "Jeruk"
            else -> ""
        }
    }
}

