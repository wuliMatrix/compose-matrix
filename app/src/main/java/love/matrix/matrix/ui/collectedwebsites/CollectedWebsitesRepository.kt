package com.kuky.demo.wan.android.ui.collectedwebsites


import com.kuky.demo.wan.android.data.PreferencesHelper
import com.kuky.demo.wan.android.entity.WebsiteData
import com.kuky.demo.wan.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.MainApplication

/**
 * @author kuky.
 * @description
 */
class CollectedWebsitesRepository(private val api: ApiService) {
    suspend fun getCollectedWebsites(): MutableList<WebsiteData>? =
        withContext(Dispatchers.IO) {
            api.collectWebsiteList(PreferencesHelper.fetchCookie(MainApplication.instance)).data
        }

    suspend fun addWebsite(name: String, link: String) =
        withContext(Dispatchers.IO) {
            api.addWebsite(name, link, PreferencesHelper.fetchCookie(MainApplication.instance))
        }

    suspend fun editWebsite(id: Int, name: String, link: String) =
        withContext(Dispatchers.IO) {
            api.editWebsite(id, name, link, PreferencesHelper.fetchCookie(MainApplication.instance))
        }

    suspend fun deleteWebsite(id: Int) =
        withContext(Dispatchers.IO) {
            api.deleteWebsite(id, PreferencesHelper.fetchCookie(MainApplication.instance))
        }
}