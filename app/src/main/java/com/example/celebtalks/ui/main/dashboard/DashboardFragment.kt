package com.example.celebtalks.ui.main.dashboard

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SimpleAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.celebtalks.R
import com.example.celebtalks.databinding.FragmentDashboardBinding
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.main.Base_Classes.BasePostFragment
import com.example.celebtalks.ui.main.Base_Classes.basePostViewModel
import com.example.celebtalks.ui.main.PostDetail.PostDetailFragment
import com.example.celebtalks.ui.snackbar
import com.example.celebtalks.utils.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Inherit from BasePostFragment
@AndroidEntryPoint
class DashboardFragment : BasePostFragment(R.id.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
    get() = _binding!!

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.fragmentDashboard

    override val basePostViewModel: basePostViewModel
        get() {
            val vm: DashboardViewModel by viewModels()
            return vm
        }

    private val dashboardViewModel : DashboardViewModel by lazy { basePostViewModel as DashboardViewModel }
    //private lateinit var dashboardViewModel : DashboardViewModel

    override val allCaughtupView: ConstraintLayout
        get() = binding.viewAllcaughtup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       // dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

         _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // setup RecyclerView
        setupRecyclerView()
        handlePagination()
        handleOnSwipeRefresh()
        setupClickListeners()
        return root
        }

    private fun handlePagination() {
        lifecycleScope.launch {
            dashboardViewModel.pagingFlow.collect{
                postAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            postAdapter.loadStateFlow.collectLatest {
                swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading ||
                        it.append is LoadState.Loading
            }
        }
    }

    private fun handleOnSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
                    handlePagination()
            }
    }

    private fun setupClickListeners() {
        // searchbar ClickListener
        binding.cardSearch.setOnClickListener {
            findNavController().navigate(R.id.action_globalActionToSearchFragment)
        }
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

