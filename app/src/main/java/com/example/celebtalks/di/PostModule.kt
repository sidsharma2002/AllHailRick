package com.example.celebtalks.di

import com.example.celebtalks.repositories.createPostRepository
import com.example.celebtalks.repositories.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn( ViewModelComponent:: class)
object PostModule {

    @ViewModelScoped
    @Provides
       fun ProvidescreatePostRepository() = createPostRepository()
}