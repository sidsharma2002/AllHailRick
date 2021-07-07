package com.example.celebtalks.ui.main.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.celebtalks.data.entities.Post
import com.example.celebtalks.data.pagingsource.FollowPostsPagingSource
import com.example.celebtalks.other.Constants.PAGE_SIZE
import com.example.celebtalks.other.Event
import com.example.celebtalks.other.Resource
import com.example.celebtalks.repositories.basePostsRepository
import com.example.celebtalks.ui.main.Base_Classes.basePostViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: basePostsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
)  : basePostViewModel(repository,dispatcher) {

    val pagingFlow = Pager(PagingConfig(PAGE_SIZE)) {
        FollowPostsPagingSource(FirebaseFirestore.getInstance())
    }.flow.cachedIn(viewModelScope)
    }
