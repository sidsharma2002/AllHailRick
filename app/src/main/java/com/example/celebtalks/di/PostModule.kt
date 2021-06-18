package com.example.celebtalks.di

import com.example.celebtalks.adapters.Postadapter
import com.example.celebtalks.repositories.createPostRepository
import com.example.celebtalks.repositories.dealwithPostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn( ViewModelComponent:: class)
object PostModule {

    @ViewModelScoped
    @Provides
       fun ProvidescreatePostRepository() = createPostRepository()

    @ViewModelScoped
    @Provides
    fun ProvidesdealwithPostRepository() = dealwithPostRepository()

}