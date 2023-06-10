package com.example.frutify.ui.dashboard.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.MainActivity
import com.example.frutify.R
import com.example.frutify.data.viewmodel.AuthViewModel
import com.example.frutify.databinding.ActivityLoginBinding
import com.example.frutify.ui.dashboard.auth.register.RegisterActivity
import com.example.frutify.utils.Constant
import com.example.frutify.utils.SharePref

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharePref: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sharePref = SharePref(this)
        disableBtnLogin()

        binding.btnLogin.setOnClickListener {

            if ((binding.edEmail.text?.length ?: 0) <= 0) {
                binding.edEmail.error = getString(R.string.invalid_email)
                binding.edEmail.requestFocus()
            }

            if (binding.edEmail.error?.length ?: 0 > 0) {
                binding.edEmail.requestFocus()
            } else if (binding.edPassword.error?.length ?: 0 > 0) {
                binding.edPassword.requestFocus()
            } else {

                val email = binding.edEmail.text.toString().trim()
                val password = binding.edPassword.text.toString().trim()
                login(email, password)

            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {

        authViewModel.login(email, password)
        authViewModel.isLoading.observe(this){ showLoading(it) }

        authViewModel.loginResult.observe(this) { login ->
            if (login?.STATUS == "SUCCESS") {
                sharePref.apply {
                    setIntPreference(
                        Constant.PREF_USER_ID,
                        login.PAYLOAD?.users?.USERID ?: 0
                    )
                    setStringPreference(
                        Constant.PREF_EMAIL,
                        login.PAYLOAD?.users?.USEREMAIL ?: ""
                    )
                    setStringPreference(
                        Constant.PREF_USER_PHONE,
                        login.PAYLOAD?.users?.USERPHONE ?: ""
                    )
                    setStringPreference(
                        Constant.PREF_USER_FULLNAME,
                        login.PAYLOAD?.users?.USERFULLNAME ?: ""
                    )
                    setStringPreference(
                        Constant.PREF_USER_ADDRESS,
                        login.PAYLOAD?.users?.USERADDRESS ?: ""
                    )
                    setStringPreference(
                        Constant.PREF_TOKEN,
                        login.PAYLOAD?.users?.USERTOKEN ?: ""
                    )
                    setStringPreference(
                        Constant.PREF_TOKEN_EXP,
                        login.PAYLOAD?.users?.USERTOKENEXPIRED ?: ""
                    )
                    setBooleanPreference(Constant.PREF_IS_LOGIN, true)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login failed. Please check your email and/or password.", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

    }

    private fun disableBtnLogin() {
        binding.btnLogin.isEnabled = binding.edPassword.text!!.toString().length >= 8
        if (binding.btnLogin.isEnabled) {
            binding.btnLogin.setBackgroundResource(R.drawable.btn_enabled)
        } else {
            binding.btnLogin.setBackgroundResource(R.drawable.btn_disabled)
        }

        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                binding.btnLogin.isEnabled = password.length >= 8
                if (binding.btnLogin.isEnabled) {
                    binding.btnLogin.setBackgroundResource(R.drawable.btn_enabled)
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.btn_disabled)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}