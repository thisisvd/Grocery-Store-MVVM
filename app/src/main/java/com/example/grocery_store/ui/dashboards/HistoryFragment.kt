package com.example.grocery_store.ui.dashboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery_store.databinding.FragmentHistoryBinding
import com.example.grocery_store.ui.adapter.CartHistoryItemAdapter
import com.example.grocery_store.ui.dashboards.viewmodel.GroceryViewModel
import com.example.grocery_store.ui.login_signup.viewmodel.CredentialsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    // view binding
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    // viewmodel
    private val groceryViewModel: GroceryViewModel by viewModels()
    private val credViewModel: CredentialsViewModel by viewModels()

    // adapter
    private lateinit var cartItemAdapter: CartHistoryItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // call items from my cart
            groceryViewModel.getCartHistoryItems(credViewModel.getLoginState())

            // init observers
            viewModelObservers()

            // init recycler view
            setupRecyclerView()
        }
    }

    // init observers
    private fun viewModelObservers() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                groceryViewModel.myCartItems.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect { cartItems ->
                    cartItemAdapter.differ.submitList(cartItems)
                    if (cartItems.isEmpty()) {
                        itemStatusTv.visibility = View.VISIBLE
                    } else {
                        itemStatusTv.visibility = View.GONE
                    }
                    progressCircular.visibility = View.GONE
                }
            }
        }
    }

    // init recycler view
    private fun setupRecyclerView() {
        binding.apply {
            cartItemAdapter = CartHistoryItemAdapter()

            recyclerView.apply {
                adapter = cartItemAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}