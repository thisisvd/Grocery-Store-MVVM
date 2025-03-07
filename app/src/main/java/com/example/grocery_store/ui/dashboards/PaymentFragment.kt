package com.example.grocery_store.ui.dashboards

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocery_store.R
import com.example.grocery_store.databinding.FragmentPaymentBinding
import com.example.grocery_store.ui.dashboards.viewmodel.GroceryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    // view binding
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    // args
    private val args: PaymentFragmentArgs by navArgs()

    // viewmodel
    private val groceryViewModel: GroceryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // init ui
            initPaymentUI()
        }
    }

    // init ui
    private fun initPaymentUI() {
        binding.apply {

            args.let {
                totalAmount.text = it.totalAmount
            }

            clickToPay.setOnClickListener {
                groceryViewModel.updateStatusAllItemFromCart(userId = args.userId) {
                    if (it) {
                        startPaymentTimer()
                    } else {
                        Snackbar.make(
                            binding.root, "Error in making payment!", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // start payment timer
    private fun startPaymentTimer() {
        binding.apply {
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            paymentTimerTv.visibility = View.VISIBLE
            descTv.visibility = View.GONE
            clickToPay.visibility = View.GONE
            // handle on back pressed d
            handleOnBackPressed()

            val timer = object : CountDownTimer(6000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000)
                    if (seconds.toInt() != 0) {
                        paymentTimerTv.text = getString(
                            R.string.payment_completing, seconds.toString()
                        )
                    } else {
                        payImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(), R.drawable.ic_checked
                            )
                        )
                        totalAmount.text = getString(R.string.payment_done)
                        totalAmountTv.visibility = View.GONE
                        paymentTimerTv.visibility = View.GONE
                    }
                }

                override fun onFinish() {
                    findNavController().navigate(
                        R.id.action_paymentFragment_to_dashboardScreen, bundleOf(
                            "userId" to args.userId
                        )
                    )
                }
            }
            timer.start()
        }
    }

    // handle onBackPressed
    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Snackbar.make(
                        binding.root,
                        "Can't press back while payment is processing.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}