package com.example.celebtalks.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.celebtalks.adapters.Postadapter
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.snackbar
import javax.inject.Inject

abstract class BasePostFragment(
    layoutId: Int
) : Fragment(layoutId) {

    @Inject
    lateinit var postAdapter: Postadapter

    protected abstract val postProgressBar: ProgressBar

    protected abstract val basePostViewModel: basePostViewModel

    private var curLikedIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        postAdapter.setOnLikeClickListener { post, i ->
            curLikedIndex = i
            post.isLiked = !post.isLiked
            basePostViewModel.toggleLikeForPost(post)
        }

//        postAdapter.setOnDeletePostClickListener { post ->
//            DeletePostDialog().apply {
//                setPositiveListener {
//                    basePostViewModel.deletePost(post)
//                }
//            }.show(childFragmentManager, null)
//        }
     }

    private fun subscribeToObservers() {
        basePostViewModel.posts.observe(viewLifecycleOwner, EventObserver(
            onError = {
                postProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = {
                postProgressBar.isVisible = true
            }
        ) { posts ->
            postProgressBar.isVisible = false
            postAdapter.posts = posts
        })
    }
}