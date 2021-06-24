package com.example.celebtalks.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.celebtalks.data.entities.User
import com.example.celebtalks.other.EventObserver

class OthersProfileFragment : ProfileFragment() {

    private val args : OthersProfileFragmentArgs by navArgs()
    override val  uid : String
        get() = args.uid
    private var curUser: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.btnToggleFollow.setOnClickListener {
            viewModel.toggleFollowForUser(uid)
        }
    }

    private fun subscribeToObservers(){
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver{
            Log.d("OtherProfileFragment ", "subscribeToObservers: ")
            binding.btnToggleFollow.isVisible = true
            // TODO(" Try this transition when app is running ")
            // setupToggleFollowButton(it)
                curUser = it
                binding.btnToggleFollow.text = when(curUser?.isfollowing){
                    true->"unfollow"
                    false->"follow"
                    else -> null
                }
        })

        viewModel.followStatus.observe(viewLifecycleOwner, EventObserver {
            Log.d("OtherProfileFrag : ", "followstatus is  $it")
            curUser?.isfollowing = it
            binding.btnToggleFollow.text = when (it) {
                true -> "unfollow"
                false -> "follow"
            }
        })
    }
}


