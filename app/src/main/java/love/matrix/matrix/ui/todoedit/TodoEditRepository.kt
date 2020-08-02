package com.kuky.demo.wan.android.ui.todoedit

import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.MainApplication

/**
 * @author kuky.
 * @description
 */
class TodoEditRepository(private val api: ApiService) {

    suspend fun addTodo(param: HashMap<String, Any>) =
        withContext(Dispatchers.IO) {
            api.addTodo(param, PreferencesHelper.fetchCookie(MainApplication.instance))
        }

    suspend fun updateTodo(id: Int, param: HashMap<String, Any>) =
        withContext(Dispatchers.IO) {
            api.updateTodo(id, PreferencesHelper.fetchCookie(MainApplication.instance), param)
        }

    suspend fun deleteTodo(id: Int) =
        withContext(Dispatchers.IO) {
            api.deleteTodo(id, PreferencesHelper.fetchCookie(MainApplication.instance))
        }
}