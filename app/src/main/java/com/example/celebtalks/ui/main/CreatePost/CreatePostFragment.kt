package com.example.celebtalks.ui.main.CreatePost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentCreatepostBinding
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.slideUpViews
import com.example.celebtalks.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private lateinit var viewModel: createPostViewModel
    private var _binding : FragmentCreatepostBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(createPostViewModel::class.java)
        _binding = FragmentCreatepostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        animateUI()
        subscribetoObservers()

            binding.btnPost.setOnClickListener {
                viewModel.createPost(
                    binding.etPostHeading.text.toString(),
                    binding.etPostDescription.text.toString()
                )
            }

        return root
    }

    private fun animateUI() {
        //slideUpViews(requireContext(), binding.btnPost)
    }

    private fun subscribetoObservers() {
         viewModel.createPostStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.createPostProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { binding.createPostProgressBar.isVisible = true }
        ) {
                 binding.createPostProgressBar.isVisible = false
                 findNavController().popBackStack()
                snackbar(getString(R.string.post_uploadsuccess))
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}