package com.example.frutify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.frutify.databinding.ActivityMainBinding
import com.example.frutify.ui.dashboard.cart.CartActivity
import com.example.frutify.ui.dashboard.edit.EditActivity
import com.example.frutify.ui.dashboard.home.buyer.HomeBuyerFragment
import com.example.frutify.ui.dashboard.home.seller.HomeSellerFragment
import com.example.frutify.ui.dashboard.profile.ProfileFragment
import com.example.frutify.utils.Helper
import com.example.frutify.utils.SharePref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharePref: SharePref

    private val TAG = Helper.DEBUG_TAG
    private var sellerMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navigation bar
        binding.navbarView.background = null
        binding.navbarView.menu[1].isEnabled = false

        sharePref = SharePref(this)

        //fragment navbar
        val profileFragment = ProfileFragment()
        val homeSellerFragment = HomeSellerFragment()
        val homeBuyerFragment = HomeBuyerFragment()

        sellerMode = sharePref.getUserRoles == "SELLER"

        //mengganti fragment roles
        val defaultFragment = if (sellerMode) {
            homeSellerFragment
        } else {
            homeBuyerFragment
        }

        switchFragment(defaultFragment)

        binding.navbarView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    val selectedFragment = if (sellerMode) {
                        homeSellerFragment
                    } else {
                        homeBuyerFragment
                    }
                    switchFragment(selectedFragment)
                    true
                }
                R.id.profile -> {
                    switchFragment(profileFragment)
                    true
                }
                else -> false
            }
        }

        Log.d(TAG, "onCreate: " + sellerMode)
        if(sellerMode){
            binding.fab.setOnClickListener { startActivity(Intent(this, EditActivity::class.java)) }
        }else{
            binding.fab.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_shopping_cart)) // gak bisa
            binding.fab.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
        }

    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


}