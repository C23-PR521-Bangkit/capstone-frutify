package com.example.frutify.ui.dashboard.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.R
import com.example.frutify.data.viewmodel.AuthViewModel
import com.example.frutify.databinding.ActivityRegisterBinding
import com.example.frutify.ui.dashboard.auth.login.LoginActivity
import com.example.frutify.utils.SharePref

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharePref: SharePref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharePref = SharePref(this)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        disableBtnLogin()

        binding.btnRegister.setOnClickListener {

             if ((binding.edEmail.text?.length ?: 0) <= 0) {
                binding.edEmail.error = getString(R.string.invalid_email)
                binding.edEmail.requestFocus()
            } else if ((binding.edPassword.text?.length ?: 0) <= 0) {
                binding.edPassword.error = getString(R.string.invalid_password)
                binding.edPassword.requestFocus()
            } else if ((binding.edPhone.text?.length ?: 0) <= 0) {
                 binding.edPhone.error = getString(R.string.wrong_number)
                 binding.edPassword.requestFocus()
            }

            if ((binding.edEmail.error?.length ?: 0) > 0) {
                binding.edEmail.requestFocus()
            } else if ((binding.edPassword.error?.length ?: 0) > 0) {
                binding.edPassword.requestFocus()
            }else if ((binding.edPhone.error?.length ?: 0) > 0){
                binding.edPhone.requestFocus()
            } else {

                val email = binding.edEmail.text.toString().trim()
                val password = binding.edPassword.text.toString().trim()
                val phone = binding.edPhone.text.toString().trim()

                val role = sharePref.getRoles
                register(email, password, phone, role!!)
            }
        }

    }

    private fun register(email: String, password: String, phone: String, role: String) {

        authViewModel.register(email, password, phone, role)
        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        authViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
        authViewModel.registerResult.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun disableBtnLogin() {
        binding.btnRegister.isEnabled = binding.edPassword.text!!.toString().length >= 8
        if (binding.btnRegister.isEnabled) {
            binding.btnRegister.setBackgroundResource(R.drawable.btn_enabled)
        } else {
            binding.btnRegister.setBackgroundResource(R.drawable.btn_disabled)
        }

        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                binding.btnRegister.isEnabled = password.length >= 8
                if (binding.btnRegister.isEnabled) {
                    binding.btnRegister.setBackgroundResource(R.drawable.btn_enabled)
                } else {
                    binding.btnRegister.setBackgroundResource(R.drawable.btn_disabled)
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