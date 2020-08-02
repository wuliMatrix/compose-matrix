package com.kuky.demo.wan.android.ui.usershared

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
class UserSharedRepository(private val api: ApiService) {

    suspend fun fetchUserSharedArticles(userId: Int, page: Int): MutableList<UserArticleDetail>? =
        withContext(Dispatchers.IO) {
            api.sharedUserInfo(
                userId, page, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data.shareArticles.datas
        }

    suspend fun fetchUserCoinInfo(userId: Int) =
        withContext(Dispatchers.IO) {
            api.sharedUserInfo(
                userId, 1, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data
        }
}