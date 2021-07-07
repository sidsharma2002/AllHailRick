package com.example.celebtalks.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.celebtalks.data.entities.Post
import com.example.celebtalks.data.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ProfilePostsPagingSource(
    private val db: FirebaseFirestore,
    private val uid: String
) : PagingSource<QuerySnapshot, Post>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val curPage = params.key ?: db.collection("posts")
                .whereEqualTo("authorUid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()

            val lastDocumentSnapshot = curPage.documents[curPage.size() - 1]

            val nextPage = db.collection("posts")
                .whereEqualTo("authorUid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                curPage.toObjects(Post::class.java).onEach { post ->
                    val user = db.collection("users").document(uid).get().await().toObject(User::class.java)!!
                    post.authorUsername = user.type
                    val isLiked_init = post.likedBy.find { item -> item == uid }
                    post.isLiked = when (isLiked_init) {
                        null -> false
                        else -> true
                    }
                },
                null,
                nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        TODO("Not yet implemented")
    }
}