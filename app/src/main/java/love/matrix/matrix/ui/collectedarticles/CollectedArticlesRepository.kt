package com.kuky.demo.wan.android.ui.collectedarticles


import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.entity.UserCollectDetail
import com.kuky.demo.wan.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.MainApplication

/**
 * @author kuky.
 * @description
 */
class CollectedArticlesRepository(private val api: ApiService) {
    suspend fun getCollectedArticleList(page: Int): MutableList<UserCollectDetail>? =
        withContext(Dispatchers.IO) {
            api.userCollectedArticles(
                page, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data.datas
        }

    suspend fun removeCollectedArticle(articleId: Int, originId: Int) =
        withContext(Dispatchers.IO) {
            api.unCollectCollection(
                articleId, originId, PreferencesHelper.fetchCookie(MainApplication.instance)
            )
        }
}