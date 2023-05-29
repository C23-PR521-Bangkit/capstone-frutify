package com.example.frutify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.frutify.databinding.ActivityMainBinding
import com.example.frutify.ui.dashboard.edit.EditActivity
import com.example.frutify.ui.dashboard.home.HomeFragment
import com.example.frutify.ui.dashboard.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navigation bar
        binding.navbarView.background = null
        binding.navbarView.menu.get(1).isEnabled = false

        //fragment navbar
        val profileFragment = ProfileFragment()
        val homeFragment = HomeFragment()

        switchFragment(homeFragment)

        binding.navbarView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    switchFragment(homeFragment)
                    true
                }
                R.id.profile -> {
                    switchFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
        binding.fab.setOnClickListener { startActivity(Intent(this, EditActivity::class.java)) }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


}