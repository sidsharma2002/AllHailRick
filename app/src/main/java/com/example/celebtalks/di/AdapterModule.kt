package com.example.celebtalks.di

import com.example.celebtalks.adapters.Postadapter
import com.example.celebtalks.adapters.Useradapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn( FragmentComponent:: class)
object AdapterModule {

        @FragmentScoped
        @Provides
        fun ProvidesPostadapter() = Postadapter()

        @FragmentScoped
        @Provides
        fun ProvidesUseradapter() = Useradapter()

}