package com.example.frutify.ui.dashboard.home.buyer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.frutify.R
import com.example.frutify.data.model.ProductItemBuyer
import com.example.frutify.data.viewmodel.CartViewModel
import com.example.frutify.databinding.ActivityDetailBuyerBinding
import com.example.frutify.ui.dashboard.cart.CartJavaActivity
import com.example.frutify.utils.Constant
import com.example.frutify.utils.SharePref

class DetailBuyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBuyerBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var sharePref: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        sharePref = SharePref(this)

        val productBuyer = intent.getParcelableExtra<ProductItemBuyer>("product")

        val imageUrl = Constant.BASE_URL_2 +"uploads?path=" +  productBuyer?.PRODUCTFILEPATH

        binding.apply {
            Glide.with(this@DetailBuyerActivity)
                .load(imageUrl)
                .error(R.drawable.apel)
                .into(imageFruit)
            tvPriceBuyer.text = "Rp " + productBuyer?.PRODUCTPRICE.toString()
            textFruitType.text = productBuyer?.FRUITNAME
            textNameSeller.text = productBuyer?.USERFULLNAME
            textFruitName.text = productBuyer?.PRODUCTNAME
            tvQuality.text = productBuyer?.PRODUCTQUALITY
            textDesc.text = productBuyer?.PRODUCTDESCRIPTION
            val namaa = productBuyer?.USERFULLNAME
            Log.e("erree", namaa!!)
        }

        binding.btnAdd.setOnClickListener {
            addToCart(sharePref.getUserId, productBuyer?.PRODUCTID!!, 1)
        }

    }

    fun addToCart(userId: Int, productId: Int, qty: Int){
        cartViewModel.addToCart(userId, productId, qty)
        cartViewModel.addCartResult.observe(this) {
            Toast.makeText(this, it?.MESSAGE, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CartJavaActivity::class.java))
        }
    }
}