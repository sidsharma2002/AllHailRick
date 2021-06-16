package com.example.celebtalks.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentRegisterBinding
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.auth.AuthViewModel
import com.example.celebtalks.ui.main.dashboard.DashboardViewModel
import com.example.celebtalks.ui.snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var viewmodel: AuthViewModel
    private var _binding: FragmentRegisterBinding ? = null
    // This property is only valid between onCreateView and
   // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewmodel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        // Observe the Live Data
        subscribeToObserver()

        // when register button is clicked
        binding.btnRegister.setOnClickListener {
               viewmodel.register(
                binding.etEmail.text.toString(),
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRepeatPassword.text.toString()
            )
        }

        // if tvLogin is clicked go to  login fragment
        binding.tvLogin.setOnClickListener{
            if(findNavController().previousBackStackEntry!=null){
                findNavController().popBackStack()
            } else
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

private fun subscribeToObserver(){
        viewmodel.registerStatus.observe(viewLifecycleOwner, EventObserver (
                    onError = {
                              binding.registerProgressBar.isVisible = false
                              snackbar(it)
                    } ,
                    onLoading = {
                        binding.registerProgressBar.isVisible = true
                    }
                ) {
                    binding.registerProgressBar.isVisible = false
                    snackbar(getString(R.string.success_registration))
        })
}








}