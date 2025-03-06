package com.example.grocery_store.ui.login_signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.grocery_store.core.Resource
import com.example.grocery_store.databinding.FragmentSignUpBinding
import com.example.grocery_store.domain.User
import com.example.grocery_store.ui.login_signup.viewmodel.CredentialsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    // view binding
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: CredentialsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // on click listeners
            onClickListeners()

            // init observers
            viewModelObservers()
        }
    }

    // on click listeners
    private fun onClickListeners() {
        binding.apply {

            signup.setOnClickListener {
                if (!userNameTv.text.isNullOrEmpty() && !passwordTv.text.isNullOrEmpty()) {
                    viewModel.addUsers(
                        User(
                            username = userNameTv.text.toString(),
                            password = passwordTv.text.toString()
                        )
                    )
                    hideKeyboard()
                } else {
                    Snackbar.make(root, "Enter all fields!", Snackbar.LENGTH_SHORT).show()
                }
            }

            login.setOnClickListener {
                hideKeyboard()
                findNavController().popBackStack()
            }
        }
    }

    // init observers
    private fun viewModelObservers() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userData.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            progressCircular.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            response.data?.let { user ->
                                val action =
                                    SignUpFragmentDirections.actionSignUpFragmentToDashboardScreen(
                                        user.userId
                                    )
                                findNavController().navigate(action)
                                viewModel.clearCredentials()
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

    // hide keyboard
    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}