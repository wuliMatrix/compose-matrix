package com.instagram.clone.utils111

import com.instagram.clone.di.NetworkApi
import com.instagram.clone.models.Users222

class UserRepository(private val service: NetworkApi) {
    suspend fun getListUsers(): Users222 = service.getListUser("1")
}