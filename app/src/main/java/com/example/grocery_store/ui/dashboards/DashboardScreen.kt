package com.example.grocery_store.ui.dashboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocery_store.R
import com.example.grocery_store.core.Resource
import com.example.grocery_store.databinding.FragmentDashboardScreenBinding
import com.example.grocery_store.domain.GroceryCartItem
import com.example.grocery_store.ui.adapter.GroceryItemAdapter
import com.example.grocery_store.ui.dashboards.viewmodel.GroceryViewModel
import com.example.grocery_store.ui.login_signup.viewmodel.CredentialsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardScreen : Fragment() {

    // view binding
    private var _binding: FragmentDashboardScreenBinding? = null
    private val binding get() = _binding!!

    // viewmodel
    private val groceryViewModel: GroceryViewModel by viewModels()
    private val credentialsViewModel: CredentialsViewModel by viewModels()

    // adapter
    private lateinit var groceryItemAdapter: GroceryItemAdapter

    // back press count var
    private var onBackPressCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardScreenBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            groceryViewModel.groceryItems()

            // init observers
            viewModelObservers()

            // handle on back pressed
            handleOnBackPressed()

            // add menu provides
            addMenuProvider()

            // init recycler view
            setupRecyclerView()
        }
    }

    // init observers
    private fun viewModelObservers() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                groceryViewModel.groceryItems.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            progressCircular.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            response.data?.let { groceryItems ->
                                groceryItemAdapter.differ.submitList(groceryItems)
                            }
                            progressCircular.visibility = View.GONE
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                progressCircular.visibility = View.GONE
                                Snackbar.make(
                                    root, message, Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }

                        null -> {}
                    }
                }
            }
        }
    }

    // init recycler view
    private fun setupRecyclerView() {
        binding.apply {
            groceryItemAdapter = GroceryItemAdapter()

            recyclerView.apply {
                adapter = groceryItemAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }

            groceryItemAdapter.setOnItemClickListener { item ->
                groceryViewModel.updateItemInCart(
                    GroceryCartItem(
                        itemId = item.itemId,
                        itemName = item.itemName,
                        itemDescription = item.itemDescription,
                        itemPrice = item.itemPrice,
                        itemImage = item.itemImage,
                        userId = credentialsViewModel.getLoginState()
                    ), true
                ) { it ->
                    Snackbar.make(
                        root,
                        if (it) "${item.itemName} added in cart!" else "Error in adding item in cart!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // handle onBackPressed
    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressCount++
                    if (onBackPressCount == 1) {
                        Snackbar.make(
                            binding.root,
                            "Press back again to close the app.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        requireActivity().finish()
                    }
                }
            })
    }

    // init menu
    private fun addMenuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)

                // get the search item and its SearchView
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                // handle search query input
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        groceryViewModel.searchInGroceryItems(newText.toString())
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }

                    R.id.action_cart -> {
                        findNavController().navigate(DashboardScreenDirections.actionDashboardScreenToAddItemsCartFragment())
                        true
                    }

                    R.id.action_logout -> {
                        credentialsViewModel.logout()
                        findNavController().popBackStack()
                        true
                    }

                    else -> {
                        true
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}