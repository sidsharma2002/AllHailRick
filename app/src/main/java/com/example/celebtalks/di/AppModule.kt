package com.example.celebtalks.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn ( SingletonComponent :: class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context : Context
    ) = context

    @Singleton
    @Provides
    // Treat Dispatcher Main as CoroutineDispatcher
    fun provideMainDispatcher() = Dispatchers.Main as CoroutineDispatcher

    //TODO("Add for Glide")
}
