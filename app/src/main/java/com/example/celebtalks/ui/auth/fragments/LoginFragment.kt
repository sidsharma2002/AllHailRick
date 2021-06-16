package com.example.celebtalks.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.celebtalks.MainActivity
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentLoginBinding
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.auth.AuthViewModel
import com.example.celebtalks.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(){

    private lateinit var  viewModel : AuthViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

      // This property is only valid between onCreateView and
      // onDestroyView.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

          viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false )
        val root: View = binding.root

          subscribeToObservers()

          binding.btnLogin.setOnClickListener{
                viewModel.login(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
          }


        // when tvRegisterNewAccount is clicked Navigate to register fragment
        binding.tvRegisterNewAccount.setOnClickListener {
                if(findNavController().previousBackStackEntry!=null){
                    findNavController().popBackStack()
                } else Navigation.findNavController(root).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return root
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.loginProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { binding.loginProgressBar.isVisible = true }
        ) {
            binding.loginProgressBar.isVisible = false
            Intent(requireContext(), MainActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }

