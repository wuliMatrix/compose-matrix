package com.kuky.demo.wan.android.ui.usersharelist


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
class UserShareListRepository(private val api: ApiService) {

    suspend fun fetchUserShareList(page: Int): MutableList<UserArticleDetail>? =
        withContext(Dispatchers.IO) {
            api.userShareList(
                page, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data.shareArticles.datas
        }

    suspend fun deleteShare(id: Int) =
        withContext(Dispatchers.IO) {
            api.deleteAShare(
                id, PreferencesHelper.fetchCookie(MainApplication.instance)
            )
        }

    suspend fun shareArticle(title: String, link: String) =
        withContext(Dispatchers.IO) {
            api.putAShare(
                title, link, PreferencesHelper.fetchCookie(MainApplication.instance)
            )
        }
}