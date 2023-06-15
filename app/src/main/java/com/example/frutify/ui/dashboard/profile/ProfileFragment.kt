package com.example.frutify.ui.dashboard.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.R
import com.example.frutify.data.viewmodel.AuthViewModel
import com.example.frutify.data.viewmodel.ProductViewModel
import com.example.frutify.databinding.FragmentHomeSellerBinding
import com.example.frutify.databinding.FragmentProfileBinding
import com.example.frutify.ui.dashboard.auth.ChooseRolesActivity
import com.example.frutify.ui.dashboard.auth.login.LoginActivity
import com.example.frutify.utils.Constant
import com.example.frutify.utils.SharePref


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharePref: SharePref
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        sharePref = SharePref(requireContext())

        binding.profileName.setText(sharePref.getFullname)
        binding.profileEmail.setText(sharePref.getEmail)
        binding.etEmail.setText(sharePref.getEmail)
        binding.etPhone.setText(sharePref.getPhone)
        binding.etFullname.setText(sharePref.getFullname)
        binding.etAddress.setText(sharePref.getAddress)

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sharePref = SharePref(requireContext())

        authViewModel.error.observe(viewLifecycleOwner) { error ->
            // Show error message
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        authViewModel.updateResult.observe(viewLifecycleOwner){ result ->
            Toast.makeText(requireContext(), result?.MESSAGE, Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveProfile.setOnClickListener {
            if (validate())
            updateUser(
                binding.etEmail.text.toString(),
                binding.etOldPassword.text.toString(),
                binding.etPhone.text.toString(),
                binding.etFullname.text.toString(),
                binding.etAddress.text.toString(),
                binding.etNewPassword.text.toString()
            )
        }

        binding.btnLogout.setOnClickListener {
            openLogoutDialog()
        }

    }

    private fun updateUser(
        email: String,
        password: String,
        phone: String,
        fullname: String,
        address: String,
        newPassword: String? = null
    ){
        authViewModel.updateUser(email, password, phone, fullname, address, newPassword)

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        binding.etEmail.setText(email)
        binding.etPhone.setText(phone)
        binding.etFullname.setText(fullname)
        binding.etAddress.setText(address)
        binding.etNewPassword.setText(newPassword)

        saveUpdatedValues(email, phone, fullname, address, newPassword)


    }

    private fun saveUpdatedValues(
        email: String,
        phone: String,
        fullname: String,
        address: String,
        newPassword: String?
    ) {

        sharePref.setStringPreference(Constant.PREF_EMAIL, email)
        sharePref.setStringPreference(Constant.PREF_USER_PHONE, phone)
        sharePref.setStringPreference(Constant.PREF_USER_FULLNAME, fullname)
        sharePref.setStringPreference(Constant.PREF_USER_ADDRESS, address)
    }

    private fun openLogoutDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Confirm Logout")
            ?.setPositiveButton("yes") { _, _ ->
                sharePref.clearPreferences()
                val intent = Intent(requireContext(), ChooseRolesActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            ?.setNegativeButton("no", null)
        val alert = alertDialog.create()
        alert.show()
    }

    private fun validate() : Boolean{
        if ((binding.etEmail.text?.length ?: 0) <= 0) {
            binding.etEmail.error = getString(R.string.invalid_email)
            binding.etEmail.requestFocus()
            return false
        } else if ((binding.etOldPassword.text?.length ?: 0) <= 0) {
            binding.etOldPassword.error = getString(R.string.invalid_password)
            binding.etOldPassword.requestFocus()
            return false
        } else if ((binding.etPhone.text?.length ?: 0) <= 0) {
            binding.etPhone.error = getString(R.string.wrong_number)
            binding.etPhone.requestFocus()
            return false
        }else if ((binding.etFullname.text?.length ?: 0) <= 0) {
            binding.etFullname.error = getString(R.string.wrong_number)
            binding.etFullname.requestFocus()
            return false
        }else if ((binding.etAddress.text?.length ?: 0) <= 0) {
            binding.etAddress.error = getString(R.string.wrong_number)
            binding.etAddress.requestFocus()
            return false
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}