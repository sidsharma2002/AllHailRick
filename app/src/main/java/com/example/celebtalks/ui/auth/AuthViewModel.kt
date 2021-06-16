package com.example.celebtalks.ui.auth

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.celebtalks.R
import com.example.celebtalks.other.Constants.MAX_USERNAME_LENGTH
import com.example.celebtalks.other.Constants.MIN_PASSWORD_LENGTH
import com.example.celebtalks.other.Constants.MIN_USERNAME_LENGTH
import com.example.celebtalks.other.Event
import com.example.celebtalks.other.Resource
import com.example.celebtalks.repositories.AuthRepository
import com.google.firebase.auth.AuthResult
import com.squareup.okhttp.Dispatcher
import  dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
        private val repository: AuthRepository ,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel () {

        // Good practice to create separate variable  of MutableLD then equalize that to LiveData
        private val _registerStatus = MutableLiveData<Event<Resource<AuthResult>>>()
        // This registerStatus is accessed in RegisterFragment through ViewModel object
        val registerStatus : LiveData<Event<Resource<AuthResult>>> = _registerStatus

        private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
        val loginStatus: LiveData<Event<Resource<AuthResult>>> = _loginStatus

        fun login(email: String, password: String) {
                if(email.isEmpty() || password.isEmpty()) {
                        val error =  "Fields must not be empty!"
                        _loginStatus.postValue(Event(Resource.Error(error)))
                } else {
                        _loginStatus.postValue(Event(Resource.Loading()))
                        viewModelScope.launch(dispatcher) {
                                val result = repository.login(email, password)
                                _loginStatus.postValue(Event(result))
                        }
                }
        }

        fun register(email : String, username : String, password : String, repeatedPassword : String) {
                // Handle errors
                val error = if(email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                        "empty field"
                } else if(password != repeatedPassword) {
                        "passwords doesn't match"
                } else if(username.length < MIN_USERNAME_LENGTH) {
                        "less than 3 char"
                } else if(username.length > MAX_USERNAME_LENGTH) {
                        "greater than 8 char"
                } else if(password.length < MIN_PASSWORD_LENGTH) {
                        "Too short"
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        "Password must be atleast 8 char long"
                } else null

                error?.let {
                        // check if error has occurred , if true then put it in  _registerStatus
                        _registerStatus.postValue(Event(Resource.Error(it)))
                        return
                }
                _registerStatus.postValue(Event(Resource.Loading( )))
                viewModelScope.launch(dispatcher) {
                        // Get result from DefaultAuthRepository
                        // 'Communication with the repository' Part
                        val result = repository.register(email, username, password)
                        _registerStatus.postValue(Event(result))
                }
        }

}






