package com.example.celebtalks.ui.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentDashboardBinding
import com.example.celebtalks.ui.main.Base_Classes.BasePostFragment
import com.example.celebtalks.ui.main.Base_Classes.basePostViewModel
import dagger.hilt.android.AndroidEntryPoint

// Inherit from BasePostFragment
@AndroidEntryPoint
class DashboardFragment : BasePostFragment(R.id.fragment_dashboard) {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
         _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Setup UI
        setupRecyclerView()

        // when CreatePost Button is  clicked
        binding.btnGotoCreatePost.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_dashboard_to_createPostFragment)
        }
            return root
        }

     override val postProgressBar : ProgressBar
        // set ProgressBar equal to this progressbar
        get() = binding.allPostsProgressBar

    override val basePostViewModel: basePostViewModel
        get() {
            // set ViewModel equal to this
            val vm: DashboardViewModel by viewModels()
            return vm
        }

    // setup RecyclerView
    private fun setupRecyclerView()  = binding.rvAllPosts.apply{
        adapter = postAdapter
        layoutManager = LinearLayoutManager(requireContext())
        // creates a glitch if item updates when not set to null
        itemAnimator = null
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }