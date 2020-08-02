package com.instagram.clone.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.instagram.clone.models.AppState
import com.instagram.clone.models.Users222
import com.instagram.clone.models.ViewState
import com.instagram.clone.utils111.UserRepository

class HomeViewModel(private val repository :UserRepository) : ViewModel(){


    val getUsers: LiveData<Users222> = liveData {
        val result = repository.getListUsers()
        AppState.viewState = ViewState.LOADING
        emit(result)
        AppState.usersResult = result.data
        AppState.viewState = ViewState.SUCCESS
    }

}