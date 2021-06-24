package com.example.celebtalks.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.celebtalks.R
import com.example.celebtalks.data.entities.Post
import com.google.firebase.auth.FirebaseAuth
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


class Postadapter () : RecyclerView.Adapter<Postadapter.PostViewHolder>(){

    // TODO( "use base adapter" )
    // PostViewHolder Class for optimisation
    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvPostAuthor : TextView = itemView.findViewById(R.id.tvPostAuthor)
        val tvPostText: TextView = itemView.findViewById(R.id.tvPostText)
        val tvLikedBy: TextView = itemView.findViewById(R.id.tvLikedBy)
        val tvPostHeading : TextView = itemView.findViewById(R.id.tvPostHeading)
        val ibLike: ImageButton = itemView.findViewById(R.id.ibLike)
        val ibComments: ImageButton = itemView.findViewById(R.id.ibComments)
        val ibDeletePost: ImageButton = itemView.findViewById(R.id.ibDeletePost)
        val cvitemPost : View = itemView.findViewById(R.id.item_post)
        val shapeisLiked : View = itemView.findViewById(R.id.shape_liked)
    }
    // DiffUtil
    private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<Post>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    // return PostViewHolder item which is made by passing itemView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PostViewHolder {
        Log.d("ProfileAdapter : ", "in onCreateViewHolder ")
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_post,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("ProfileAdapter postsize is  : ", posts.size.toString() )
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        Log.d("ProfileAdapter : ", "in onBindViewHolder ")
        holder.apply {
            tvPostAuthor.text = post.authorUsername
            tvPostText.text = post.body
            tvPostHeading.text = post.heading
            val likeCount = post.likedBy.size

            tvLikedBy.text = when {
                likeCount <= 0 -> "No likes"
                likeCount == 1 -> "Liked by 1 person"
                else -> "Liked by $likeCount people"
            }

            val uid = FirebaseAuth.getInstance().uid!!
            // if post's ui is equal to uid of current user
            // then show  Delete post button
            ibDeletePost.isVisible = uid == post.authorUid
            // Change drawable according to liked status
            shapeisLiked.isVisible = post.isLiked == true

            // Click Listeners
            tvPostAuthor.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(post.authorUid)
                }
            }

            tvLikedBy.setOnClickListener {
                onLikedByClickListener?.let { click ->
                    click(post)
                }
            }

            cvitemPost.setOnLongClickListener{
                onLikeClickListener?.let { click ->
                    if(!post.isLiking)
                        // Allow click only when isLiking is false
                     click(post, holder.layoutPosition)
                }
                true
            }

            ibComments.setOnClickListener {
                onCommentsClickListener?.let { click ->
                    click(post)
                }
            }
            ibDeletePost.setOnClickListener {
                onDeletePostClickListener?.let { click ->
                    click(post)
                }
            }
        }
    }

    private var onLikeClickListener: ((Post, Int) -> Unit)? = null
    private var onUserClickListener: ((String) -> Unit)? = null
    private var onDeletePostClickListener: ((Post) -> Unit)? = null
    private var onLikedByClickListener: ((Post) -> Unit)? = null
    private var onCommentsClickListener: ((Post) -> Unit)? = null

    fun setOnLikeClickListener(listener: (Post, Int) -> Unit) {
        onLikeClickListener = listener
    }

    fun setOnUserClickListener(listener: (String) -> Unit) {
        onUserClickListener = listener
    }

    fun setOnDeletePostClickListener(listener: (Post) -> Unit) {
        onDeletePostClickListener = listener
    }

    fun setOnLikedByClickListener(listener: (Post) -> Unit) {
        onLikedByClickListener = listener
    }

    fun setOnCommentsClickListener(listener: (Post) -> Unit) {
        onCommentsClickListener = listener
    }

    fun onSwiped(pos: Int){
        onDeletePostClickListener?.let { click ->
            click(posts[pos])
        }
    }

}