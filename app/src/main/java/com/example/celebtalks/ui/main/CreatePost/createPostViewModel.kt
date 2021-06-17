package com.example.celebtalks.ui.main.CreatePost


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celebtalks.other.Event
import com.example.celebtalks.other.Resource
import com.example.celebtalks.repositories.createPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class createPostViewModel @Inject constructor(
    private val repository: createPostRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _createPostStatus = MutableLiveData<Event<Resource<Any>>>()
    val createPostStatus: LiveData<Event<Resource<Any>>> = _createPostStatus

    fun createPost(heading: String, body: String) {
        if (heading.isEmpty() || body.isEmpty()) {
            val error = "Empty Field !"
            _createPostStatus.postValue(Event(Resource.Error(error)))
        } else {
            _createPostStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = repository.createPost(heading, body)
                _createPostStatus.postValue(Event(result))
            }
        }
    }

}