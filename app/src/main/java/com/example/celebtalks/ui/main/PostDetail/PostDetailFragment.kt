package com.example.celebtalks.ui.main.PostDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.celebtalks.databinding.FragmentHomeBinding
import com.example.celebtalks.databinding.FragmentPostdetailBinding
import com.example.celebtalks.ui.main.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : BottomSheetDialogFragment(){

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }

    private var _binding: FragmentPostdetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         _binding = FragmentPostdetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setPostDetail()
        return root
    }

    private fun setPostDetail() {
        binding.tvPostHeadingDetail.text = "Sample Bottom Sheet detail heading"
        binding.tvPostTextDetail.text = "Sample Bottom Sheet detail text"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}