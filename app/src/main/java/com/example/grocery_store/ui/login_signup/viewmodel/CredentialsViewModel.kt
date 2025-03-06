package com.example.grocery_store.ui.login_signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery_store.core.Resource
import com.example.grocery_store.data.repository.MainRepository
import com.example.grocery_store.data.repository.PreferenceRepository
import com.example.grocery_store.domain.User
import com.example.grocery_store.utils.Constants.SP_USER_ID_INVALID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val repository: MainRepository, private val sharedPref: PreferenceRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<Resource<User>?>(null)
    val userData: StateFlow<Resource<User>?> = _userData

    fun getUsers(username: String, password: String) = viewModelScope.launch {
        _userData.emit(Resource.Loading())
        repository.getUser(username, password).collect { user ->
            if (user != null) {
                _userData.emit(Resource.Success(user))
                sharedPref.saveLoginState(user.userId)
            } else {
                _userData.emit(Resource.Error("User not found!"))
                sharedPref.saveLoginState(SP_USER_ID_INVALID)
            }
        }
    }

    fun addUsers(user: User) = viewModelScope.launch {
        val isSuccess = repository.insertUser(user)
        if (isSuccess > 0) {
            _userData.emit(Resource.Loading())
            repository.getUser(user.username, user.password).collect { user ->
                _userData.emit(Resource.Success(user))
                sharedPref.saveLoginState(user.userId)
            }
        } else {
            _userData.emit(Resource.Error("User already exists!"))
            sharedPref.saveLoginState(SP_USER_ID_INVALID)
        }
    }

    suspend fun clearCredentials() {
        _userData.emit(null)
    }

    fun logout() {
        sharedPref.saveLoginState(SP_USER_ID_INVALID)
    }

    fun getLoginState() = sharedPref.getLoginState()
}