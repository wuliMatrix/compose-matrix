package com.kuky.demo.wan.android.ui.system


import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.entity.WxChapterListDatas
import com.kuky.demo.wan.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.MainApplication

/**
 * @author kuky.
 * @description
 */
class KnowledgeSystemRepository(private val api: ApiService) {
    suspend fun loadSystemType() =
        withContext(Dispatchers.IO) {
            api.knowledgeSystem().data
        }

    suspend fun loadArticle4System(page: Int, cid: Int): MutableList<WxChapterListDatas>? =
        withContext(Dispatchers.IO) {
            api.articleInCategory(
                page, cid, PreferencesHelper.fetchCookie(MainApplication.instance)
            ).data.datas
        }
}