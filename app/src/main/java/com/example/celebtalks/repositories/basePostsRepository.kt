package com.example.celebtalks.repositories

import android.util.Log
import com.example.celebtalks.data.entities.Post
import com.example.celebtalks.data.entities.User
import com.example.celebtalks.other.Resource
import com.example.celebtalks.other.safeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

@ViewModelScoped
class basePostsRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val users = firestore.collection("users")
    private val posts = firestore.collection("posts")
    private val comments = firestore.collection("comments")


    suspend fun getPostsForProfile(uid: String) = withContext(Dispatchers.IO) {
        safeCall {
            Log.d(" basePostRepository ", " getPostsForProfile is called  ")
            val Currentuid = FirebaseAuth.getInstance().uid!!
            // get Posts where authorUid is equal to uid
            val profilePosts = posts.whereEqualTo("authorUid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Post::class.java)
                .onEach { post ->
                    Log.d(" basepostRepository : getPostforProfile ", post.authorUid)
                    val user = getUser(post.authorUid).data!!
                    post.authorUsername = user.type
                    val  isLiked_init = post.likedBy.find { item -> item == Currentuid}
                    post.isLiked = when (isLiked_init) {
                        null -> false
                        else -> true
                    }
                }
            Resource.Success(profilePosts)
        }
    }

    suspend fun searchUser(query: String) = withContext(Dispatchers.IO) {
        safeCall {
            val userResults =
                users.whereGreaterThanOrEqualTo("uid", query)
                    .get().await().toObjects(User::class.java)
            Resource.Success(userResults)
        }
    }

     suspend fun getPostsForFollows() = withContext(Dispatchers.IO) {
         safeCall {
             val uid = FirebaseAuth.getInstance().uid!!
             val follows = getUser(uid).data!!.follows
             val allPosts = posts.whereIn("authorUid", follows)
                 .orderBy("date", Query.Direction.DESCENDING)
                 .get()
                 .await()
                 .toObjects(Post::class.java)
                 .onEach { post ->
                     val user = getUser(post.authorUid).data!!
                     post.authorUsername = user.type
                     val isLiked_init = post.likedBy.find { item -> item == uid }
                     post.isLiked = when (isLiked_init) {
                         null -> false
                         else -> true
                     }
                 }
             Resource.Success(allPosts)
         }
     }

     suspend fun toggleFollowForUser(uid: String) = withContext(Dispatchers.IO) {
        safeCall {
            var isFollowing = false
            firestore.runTransaction { transaction ->
                val currentUid = auth.uid!!
                val currentUser =
                    transaction.get(users.document(currentUid)).toObject(User::class.java)!!
                isFollowing = uid in currentUser.follows
                val newFollows =
                    if (isFollowing) currentUser.follows - uid else currentUser.follows + uid
                transaction.update(users.document(currentUid), "follows", newFollows)
            }.await()
            Resource.Success(!isFollowing)
        }
    }

      suspend fun toggleLikeForPost(post: Post) = withContext(Dispatchers.IO) {
        safeCall {
            var isLiked = false
            firestore.runTransaction { transaction ->
                val uid = FirebaseAuth.getInstance().uid!!
                val postResult = transaction.get(posts.document(post.id))
                val currentLikes = postResult.toObject(Post::class.java)?.likedBy ?: listOf()
                transaction.update(
                    posts.document(post.id),
                    "likedBy",
                    if (uid in currentLikes) currentLikes - uid else {
                        isLiked = true
                        currentLikes + uid
                    }
                )
            }.await()
            Resource.Success(isLiked)
        }
    }

    suspend fun deletePost(post: Post) = withContext(Dispatchers.IO) {
        safeCall {
            posts.document(post.id).delete().await()
            Resource.Success(post)
        }
    }

    suspend fun getUsers(uids: List<String>) = withContext(Dispatchers.IO) {
        safeCall {
            val chunks = uids.chunked(10)
            val resultList = mutableListOf<User>()
            chunks.forEach { chunk ->
                val usersList = users.whereIn("uid", uids).orderBy("type").get().await()
                    .toObjects(User::class.java)
                resultList.addAll(usersList)
            }
            Resource.Success(resultList.toList())
        }
    }

    suspend fun getUser(uid: String) = withContext(Dispatchers.IO) {
        safeCall {
            val currentUid = FirebaseAuth.getInstance().uid!!
            val user = users.document(uid).get().await().toObject(User::class.java)
                ?: throw IllegalStateException()
            val currentUser = users.document(currentUid).get().await().toObject(User::class.java)
                ?: throw IllegalStateException()
            val  isfollowed_init = currentUser.follows.find { item -> item == uid}
            user.isfollowing = when (isfollowed_init) {
                null -> false
                else -> true
            }
            Resource.Success(user)
        }
    }
}