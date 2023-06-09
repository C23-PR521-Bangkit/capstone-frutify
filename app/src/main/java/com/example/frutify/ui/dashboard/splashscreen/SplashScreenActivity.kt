package com.example.frutify.ui.dashboard.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.frutify.MainActivity
import com.example.frutify.R
import com.example.frutify.databinding.ActivitySplashScreenBinding
import com.example.frutify.ui.dashboard.auth.login.LoginActivity
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
        token = "bearer ${sharePref.getToken}"
        if (sharePref.isLogin == false) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}