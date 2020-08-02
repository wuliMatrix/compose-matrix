package com.kuky.demo.wan.android.ui.userarticles


import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.entity.UserArticleDetail
import com.kuky.demo.wan.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.MainApplication

/**
 * @author kuky.
 * @description
 */
class UserArticleRepository(private val api: ApiService) {
    suspend fun fetchUserArticles(page: Int): MutableList<UserArticleDetail>? =
        withContext(Dispatchers.IO) {
            api.shareArticles(
                page, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data.datas
        }
}