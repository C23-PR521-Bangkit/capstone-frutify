package com.example.frutify.ui.dashboard.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.R
import com.example.frutify.data.viewmodel.AuthViewModel
import com.example.frutify.data.viewmodel.ProductViewModel
import com.example.frutify.databinding.FragmentHomeSellerBinding
import com.example.frutify.databinding.FragmentProfileBinding
import com.example.frutify.ui.dashboard.auth.login.LoginActivity
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sharePref = SharePref(requireContext())

        binding.btnSaveProfile.setOnClickListener {
            updateUser(
                binding.etEmail.text.toString(),
                binding.etOldPassword.text.toString(),
                binding.etPhone.text.toString(),
                binding.etFullname.text.toString(),
                binding.etAddress.text.toString(),
                binding.etNewPassword.text.toString()
            )
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
        authViewModel.updateResult.observe(viewLifecycleOwner){ result ->
            Toast.makeText(requireContext(), result?.MESSAGE, Toast.LENGTH_SHORT).show()

        }
        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        authViewModel.error.observe(viewLifecycleOwner) { error ->
            // Show error message
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}