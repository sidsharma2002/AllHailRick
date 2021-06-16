package com.example.celebtalks.di

import com.example.celebtalks.repositories.AuthRepository
import com.example.celebtalks.repositories.DefaultAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn ( ViewModelComponent :: class)
object AuthModule {

    // Lives as long as  ViewModel lives
    @ViewModelScoped
    @Provides
    // DefaultAuthRepository should be treated as AuthRepository
    fun ProvidesAuthRepository() = DefaultAuthRepository() as AuthRepository

}