package com.example.celebtalks.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentProfileBinding
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.main.Base_Classes.BasePostFragment
import com.example.celebtalks.ui.main.Base_Classes.basePostViewModel
import com.example.celebtalks.ui.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ProfileFragment : BasePostFragment(R.id.fragment_profile) {

    private lateinit var profileViewModel: ProfileViewModel
     private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
      val binding get() = _binding!!

    override val postProgressBar : ProgressBar
        // set ProgressBar equal to this progressbar
        get() = binding.profileMetaProgressBar

    override val basePostViewModel: basePostViewModel
        get() {
            // set ViewModel equal to this
            val vm: ProfileViewModel by viewModels()
            return vm
        }

    protected val viewModel: ProfileViewModel
        get() = basePostViewModel as ProfileViewModel

    protected open val uid: String
        get() = FirebaseAuth.getInstance().uid!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        subscribeToObservers()

        binding.btnToggleFollow.isVisible = false
        viewModel.loadProfile(uid)

        return root
    }

    private fun setupRecyclerView() = binding.rvPosts.apply {
        adapter = postAdapter
        itemAnimator = null
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        Log.d("ProfileFragment : ", "subscribeToObservers: ")
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.profileMetaProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { binding.profileMetaProgressBar.isVisible = true }
        ) { user ->
            Log.d("Log message ", "in on success ")
            binding.profileMetaProgressBar.isVisible = false
            binding.tvUsername.text = user.username
            Log.d("username after setting is ", binding.tvUsername.text.toString())
            binding.tvProfileDescription.text = if(user.description.isEmpty()) {
                requireContext().getString(R.string.no_description)
            } else user.description
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}