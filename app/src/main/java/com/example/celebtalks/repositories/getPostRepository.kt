package com.example.celebtalks.repositories

import com.example.celebtalks.data.entities.Post
import com.example.celebtalks.other.Resource
import com.example.celebtalks.other.safeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


abstract class getPostRepository  : PostRepository{

        private val  auth = FirebaseAuth.getInstance()
        private val firestore = FirebaseFirestore.getInstance()
        private val users = firestore.collection("users")
        private val posts = firestore.collection("posts")
        private val comments = firestore.collection("comments")

    override suspend fun createPost(heading: String, body: String)  = withContext(Dispatchers.IO) {
            safeCall {
                val uid = auth.uid!!
                val postId = UUID.randomUUID().toString()
                val post = Post(
                        id   = postId,
                        authorUid = uid,
                        heading = heading,
                        body = body,
                        date = System.currentTimeMillis().toString()
                )
                posts.document(postId).set(post).await()
                Resource.Success(Any())
            }
    }
}