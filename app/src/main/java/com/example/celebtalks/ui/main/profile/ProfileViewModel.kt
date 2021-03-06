package com.example.celebtalks.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.celebtalks.data.entities.Post
import com.example.celebtalks.data.entities.User
import com.example.celebtalks.data.pagingsource.ProfilePostsPagingSource
import com.example.celebtalks.other.Constants.PAGE_SIZE
import com.example.celebtalks.other.Event
import com.example.celebtalks.other.Resource
import com.example.celebtalks.repositories.basePostsRepository
import com.example.celebtalks.ui.main.Base_Classes.basePostViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val repository : basePostsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
    ) : basePostViewModel(repository , dispatcher){

    // Initialize LiveData
    private val _profileMeta = MutableLiveData<Event<Resource<User>>>()
    val profileMeta: LiveData<Event<Resource<User>>> = _profileMeta

    private val _followStatus = MutableLiveData<Event<Resource<Boolean>>>()
    val followStatus: LiveData<Event<Resource<Boolean>>> = _followStatus

    fun getPagingFlow(uid: String): Flow<PagingData<Post>> {
        val pagingSource = ProfilePostsPagingSource(
            FirebaseFirestore.getInstance(),
            uid
        )
        return Pager(PagingConfig(PAGE_SIZE)) {
            pagingSource
        }.flow.cachedIn(viewModelScope)
    }

        fun toggleFollowForUser(uid: String) {
            _followStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = repository.toggleFollowForUser(uid)
                _followStatus.postValue(Event(result))
            }
        }

        fun loadProfile(uid: String) {
            _profileMeta.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                Log.d("ProfileViewModel : ", " loadProfile is called  ")
                val result = repository.getUser(uid)
                _profileMeta.postValue(Event(result))
            }
        }

}