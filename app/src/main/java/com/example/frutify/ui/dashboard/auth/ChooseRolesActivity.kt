package com.example.frutify.ui.dashboard.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frutify.databinding.ActivityChooseRolesBinding
import com.example.frutify.ui.dashboard.auth.login.LoginActivity
import com.example.frutify.utils.Constant
import com.example.frutify.utils.SharePref

class ChooseRolesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRolesBinding
    private lateinit var sharePref: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = SharePref(this)

        binding.buttonSeller.setOnClickListener {
            saveRole("SELLER")
            navigateToLogin()
        }

        binding.buttonBuyer.setOnClickListener {
            saveRole("BUYER")
            navigateToLogin()
        }
    }

    private fun saveRole(role: String) {
        sharePref.setStringPreference(Constant.ROLES, role)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}