package com.example.frutify.ui.dashboard.home.buyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.frutify.R
import com.example.frutify.data.model.ProductItemBuyer
import com.example.frutify.databinding.ActivityDetailBuyerBinding
import com.example.frutify.utils.Helper

class DetailBuyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBuyerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productBuyer = intent.getParcelableExtra<ProductItemBuyer>("product")

        val imageUrl =
            Helper.BASE_URL + productBuyer?.PRODUCTFILEPATH

        binding.apply {
            Glide.with(this@DetailBuyerActivity)
                .load(imageUrl) // Error image if unable to load
                .into(imageFruit)
            tvPriceBuyer.text = "Rp " + productBuyer?.PRODUCTPRICE.toString()
            textFruitType.text = productBuyer?.FRUITNAME
            textNameSeller.text = productBuyer?.USERFULLNAME
            textFruitName.text = productBuyer?.PRODUCTNAME
            tvQuality.text = productBuyer?.PRODUCTQUALITY
            textDesc.text = productBuyer?.PRODUCTDESCRIPTION
        }

    }
}