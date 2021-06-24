package com.example.celebtalks.ui.main.Base_Classes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.celebtalks.adapters.Postadapter
import com.example.celebtalks.other.EventObserver
import com.example.celebtalks.ui.main.Dialogs.DeletePostDialog
import com.example.celebtalks.ui.snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
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

    private var curLikedIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        // When Post is clicked
        postAdapter.setOnLikeClickListener { post, i ->
            curLikedIndex = i
            post.isLiked = !post.isLiked
            basePostViewModel.toggleLikeForPost(post)
        }

        postAdapter.setOnDeletePostClickListener { post->
            DeletePostDialog().apply {
                    setpositiveListener {
                         basePostViewModel.deletePost(post)
                    }
            }.show(childFragmentManager , null)
        }
     }

    // This code would be same everywhere in the class which inherits from it
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
                Log.d("basepost fragment : ", " error in post.observe")
                postProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = {
                postProgressBar.isVisible = true
            }
        ) { posts ->
              Log.d("basepost fragment : ", " onSuccess ")
              postProgressBar.isVisible = false
              // set List<Post> in adapter
              postAdapter.posts = posts
        })
    }
}