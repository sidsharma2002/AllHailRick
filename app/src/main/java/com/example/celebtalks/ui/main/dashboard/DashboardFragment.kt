package com.example.celebtalks.ui.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentDashboardBinding
import com.example.celebtalks.ui.main.BasePostFragment
import com.example.celebtalks.ui.main.basePostViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        setupRecyclerView()

        binding.btnGotoCreatePost.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_dashboard_to_createPostFragment)
        }
            return root
        }

    override val postProgressBar: ProgressBar
        get() = binding.allPostsProgressBar

    override val basePostViewModel: basePostViewModel
        get() {
            val vm: DashboardViewModel by viewModels()
            return vm
        }

    private fun setupRecyclerView()  = binding.rvAllPosts.apply{
        adapter = postAdapter
        layoutManager = LinearLayoutManager(requireContext())
        itemAnimator = null
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }