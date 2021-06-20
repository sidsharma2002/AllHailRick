package com.example.celebtalks.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.celebtalks.MobileNavigationDirections
import com.example.celebtalks.R
import com.example.celebtalks.adapters.Useradapter
import com.example.celebtalks.databinding.FragmentSearchBinding
import com.example.celebtalks.other.Constants.SEARCH_TIME_DELAY
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
     @Inject
     lateinit var useradapter: Useradapter
     private lateinit var viewmodel : SearchViewModel
     private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewmodel =
            ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        subscribeToObservers()
        setupRecyclerView()

        var job : Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                it?.let{
                    viewmodel.searchUser(it.toString())
                }
            }
        }

        useradapter.setOnUserClickListener {
            findNavController()
                .navigate(
                    MobileNavigationDirections.actionGlobalActionToOthersProfileFragment(it.uid)
                )
        }

        return  root
        }



    private fun subscribeToObservers() {
        viewmodel.searchResults.observe(viewLifecycleOwner , EventObserver(
            onError = {
                binding.searchProgressBar.isVisible = false
                snackbar(it)
            } ,
            onLoading = {
                binding.searchProgressBar.isVisible = true
            }
        )  {
                binding.searchProgressBar.isVisible = false
                useradapter.users = it
        } )
    }

    private fun setupRecyclerView()  = binding.rvSearchResults.apply{
        layoutManager = LinearLayoutManager(requireContext())
        adapter = useradapter
        itemAnimator = null
    }
}