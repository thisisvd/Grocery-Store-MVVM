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
import com.example.grocery_store.R
import com.example.grocery_store.core.Resource
import com.example.grocery_store.databinding.FragmentLoginBinding
import com.example.grocery_store.ui.login_signup.viewmodel.CredentialsViewModel
import com.example.grocery_store.utils.Constants.SP_USER_ID_INVALID
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // view binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: CredentialsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // check login state and navigate
            if (viewModel.getLoginState() != SP_USER_ID_INVALID) {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToDashboardScreen(viewModel.getLoginState())
                findNavController().navigate(action)
            }

            // on click listeners
            onClickListeners()

            // init observers
            viewModelObservers()
        }
    }

    // on click listeners
    private fun onClickListeners() {
        binding.apply {

            login.setOnClickListener {
                if (!userNameTv.text.isNullOrEmpty() && !passwordTv.text.isNullOrEmpty()) {
                    viewModel.getUsers(
                        userNameTv.text.toString(), passwordTv.text.toString(),
                    )
                    hideKeyboard()
                } else {
                    Snackbar.make(root, "Enter all fields!", Snackbar.LENGTH_SHORT).show()
                }
            }

            signup.setOnClickListener {
                hideKeyboard()
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
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
                                    LoginFragmentDirections.actionLoginFragmentToDashboardScreen(
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