package com.example.frutify.ui.dashboard.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.frutify.MainActivity
import com.example.frutify.databinding.ActivitySplashScreenBinding
import com.example.frutify.ui.dashboard.auth.ChooseRolesActivity
import com.example.frutify.utils.SharePref

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val SPLASH_TIME_OUT: Long = 3000
    private lateinit var sharePref: SharePref
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = SharePref(this)
        Handler().postDelayed({
            checkLogin()
        }, SPLASH_TIME_OUT)


    }

    private fun checkLogin(){
        if (!sharePref.isLogin) {
            startActivity(Intent(this, ChooseRolesActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}