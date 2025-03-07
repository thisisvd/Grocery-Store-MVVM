package com.example.grocery_store.ui.dashboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery_store.R
import com.example.grocery_store.databinding.FragmentAddItemsCartBinding
import com.example.grocery_store.ui.adapter.CartItemAdapter
import com.example.grocery_store.ui.adapter.CartItemType
import com.example.grocery_store.ui.dashboards.viewmodel.GroceryViewModel
import com.example.grocery_store.ui.login_signup.viewmodel.CredentialsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyCartFragment : Fragment() {

    // view binding
    private var _binding: FragmentAddItemsCartBinding? = null
    private val binding get() = _binding!!

    // viewmodel
    private val groceryViewModel: GroceryViewModel by viewModels()
    private val credViewModel: CredentialsViewModel by viewModels()

    // adapter
    private lateinit var cartItemAdapter: CartItemAdapter

    // bottom sheet behavior
    private lateinit var checkoutBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemsCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // call items from my cart
            groceryViewModel.myCartItems()

            // init observers
            viewModelObservers()

            // init recycler view
            setupRecyclerView()

            // setup bottom-sheet
            checkoutBottomSheetBehavior =
                BottomSheetBehavior.from(binding.layoutCheckoutItems.checkoutBottomSheet)
            checkoutBottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.layoutCheckoutItems.apply {
                        // check for clear texts in login
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                            groceryViewModel.getTotalItemCountAndSum(credViewModel.getLoginState()) { item ->
                                item.let {
                                    val itemCount = it.totalCount
                                    val itemCountPrice = it.totalSum
                                    val totalItemGST = (itemCountPrice * 18 / 100)
                                    val finalAmount = totalItemGST + itemCountPrice

                                    totalItemCount.text = itemCount.toString()
                                    totalItemPrice.text = getString(
                                        R.string.amount_in_rupee, itemCountPrice.toString()
                                    )
                                    totalItemGstPriceTv.text =
                                        getString(R.string.amount_in_rupee, totalItemGST.toString())
                                    finalItemGst.text =
                                        getString(R.string.amount_in_rupee, finalAmount.toString())
                                    checkoutCancel.setOnClickListener {
                                        checkoutBottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_COLLAPSED
                                    }
                                    payment.setOnClickListener {
                                        checkoutBottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_COLLAPSED
                                        val action = MyCartFragmentDirections
                                            .actionAddItemsCartFragmentToPaymentFragment(
                                                credViewModel.getLoginState(),
                                                getString(
                                                    R.string.amount_in_rupee,
                                                    finalAmount.toString()
                                                )
                                            )
                                        findNavController().navigate(action)
                                    }
                                    itemView.visibility = View.VISIBLE
                                    progressCircular.visibility = View.GONE
                                }
                            }
                        }
                    }
                }

                override fun onSlide(p0: View, p1: Float) {}
            })

            checkoutItems.setOnClickListener {
                checkoutBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
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
                        checkoutItems.visibility = View.GONE
                        checkoutBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        itemStatusTv.visibility = View.VISIBLE
                    } else {
                        checkoutItems.visibility = View.VISIBLE
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
            cartItemAdapter = CartItemAdapter()

            recyclerView.apply {
                adapter = cartItemAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            cartItemAdapter.setOnItemClickListener { item, type ->
                when (type) {
                    CartItemType.ADD_ITEM_IN_CART -> {
                        groceryViewModel.updateItemInCart(item, true) {
                            // do nothing
                        }
                    }

                    CartItemType.REDUCE_ITEM_IN_CART -> {
                        groceryViewModel.updateItemInCart(item, false) {
                            // do nothing
                        }
                    }

                    CartItemType.CANCEL_ITEM -> {
                        groceryViewModel.deleteItemFromCart(item.userId, item.itemId)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}