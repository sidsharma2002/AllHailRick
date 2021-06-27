package com.example.celebtalks.ui.main.Base_Classes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.celebtalks.R
import com.example.celebtalks.adapters.Postadapter
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.main.Dialogs.DeletePostDialog
import com.example.celebtalks.ui.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Common Fragment which takes fragment layout id
//  and inherits from Fragment class
@AndroidEntryPoint
abstract class BasePostFragment(
    layoutId : Int
) : Fragment(layoutId) {

    @Inject  lateinit var postAdapter : Postadapter
    // abstract variables
    protected abstract val postProgressBar: ProgressBar
    protected abstract val basePostViewModel: basePostViewModel
    protected abstract val  allCaughtupView : ConstraintLayout?
    protected abstract val swipeRefreshLayout : SwipeRefreshLayout?

    private var curLikedIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        // Handle swipe refresh
        handleonSwipeRefresh()
        // Handle on click listeners
        handleOnClickListeners()
     }


    private fun handleOnClickListeners() {
        postAdapter.setOnLikeClickListener { post, i ->
            curLikedIndex = i
            post.isLiked = !post.isLiked
            basePostViewModel.toggleLikeForPost(post)
        }
        postAdapter.setOnPostClickListener {
            findNavController().navigate(R.id.action_globalActionToPostDetailFragment)
        }
        postAdapter.setOnDeletePostClickListener { post->
            DeletePostDialog().apply {
                setpositiveListener {
                    basePostViewModel.deletePost(post)
                }
            }.show(childFragmentManager , null)
        }
    }


    private fun handleonSwipeRefresh() {
            swipeRefreshLayout?.setOnRefreshListener {
                basePostViewModel.getPosts()
            }
    }


      private fun subscribeToObservers() {
        Log.d("Basepost Fragment : ", "subscribeToObservers: ")
          // Observe like post status
          basePostViewModel.likePostStatus.observe(viewLifecycleOwner, EventObserver(
              onError = {
                  curLikedIndex?.let { index ->
                      postAdapter.posts[index].isLiking = false
                      postAdapter.notifyItemChanged(index)
                  }
                  snackbar(it)
              },
              onLoading = {
                  curLikedIndex?.let { index ->
                      postAdapter.posts[index].isLiking = true
                      postAdapter.notifyItemChanged(index)
                  }
              }
          ) { isLiked ->
              Log.d("basepost fragment : ", " onSuccess of likepoststatus ")
              curLikedIndex?.let { index ->
                  val uid = FirebaseAuth.getInstance().uid!!
                  // change post liked by and liked at post[index]
                  postAdapter.posts[index].apply {
                      this.isLiked = isLiked
                      isLiking = false
                      if(isLiked) {
                          likedBy += uid
                      } else {
                          likedBy -= uid
                      }
                  }
                  postAdapter.notifyItemChanged(index)
              }
          })

          // Observe  delete post status
          basePostViewModel.deletePostStatus.observe(viewLifecycleOwner, EventObserver(
              onError = { snackbar(it) }
          ) { deletedPost ->
              postAdapter.posts -= deletedPost
          })

         // Observe Posts
          basePostViewModel.posts.observe(viewLifecycleOwner, EventObserver(
            onError = {
                swipeRefreshLayout?.isRefreshing = false
                snackbar(it)
            },
            onLoading = {
                swipeRefreshLayout?.isRefreshing = true
            }
        ) { posts ->
              swipeRefreshLayout?.isRefreshing = false
              if(postAdapter.posts == posts){
                  allCaughtupView?.isVisible = true
              }
              else {
                  allCaughtupView?.visibility = View.GONE
                  postAdapter.posts = posts
              }
        })
    }
}