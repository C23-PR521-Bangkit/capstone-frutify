package com.example.frutify.ui.dashboard.home.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.R
import com.example.frutify.data.viewmodel.ProductViewModel
import com.example.frutify.databinding.FragmentHomeBuyerBinding
import com.example.frutify.databinding.FragmentHomeSellerBinding
import com.example.frutify.ui.dashboard.home.seller.HomeSellerAdapter
import com.example.frutify.utils.SharePref


class HomeBuyerFragment : Fragment() {

    private var _binding: FragmentHomeBuyerBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharePref: SharePref
    private lateinit var homeBuyerAdapter: HomeBuyerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        sharePref = SharePref(requireContext())

        homeBuyerAdapter = HomeBuyerAdapter()
        binding.recyclerViewBuyer.adapter = homeBuyerAdapter

        productViewModel.getListProductBuyer(binding.etSearch.text.toString())
        productViewModel.productBuyerResult.observe(viewLifecycleOwner) { products ->
            if (products != null) {
                homeBuyerAdapter.submitList(products)
            }
        }

        binding.ivSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            getProduct(query)
        }



    }

    private fun getProduct(query: String? = null) {
        if (query.isNullOrEmpty()) {
            productViewModel.getListProductBuyer(null)
        } else {
            productViewModel.getListProductBuyer(query)
        }
    }

}