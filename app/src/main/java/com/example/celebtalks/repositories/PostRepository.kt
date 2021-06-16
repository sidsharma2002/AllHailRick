package com.example.celebtalks.repositories

import com.example.celebtalks.other.Resource
import dagger.Binds
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

interface PostRepository {
    suspend fun  createPost(heading : String , body : String) : Resource<Any>
    suspend fun  getPostsforProfile(heading : String , body : String) : Resource<Any>
}