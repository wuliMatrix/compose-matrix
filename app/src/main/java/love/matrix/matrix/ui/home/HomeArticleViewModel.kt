package com.kuky.demo.wan.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.instagram.clone.models.AppState
import com.instagram.clone.models.ViewState
import com.kuky.demo.wan.android.data.db.HomeArticleDetail
import com.kuky.demo.wan.android.ui.app.constPagerConfig

/**
 * @author kuky.
 * @description
 */
class HomeArticleViewModel(
    private val repository: HomeArticleRepository
) : ViewModel() {

    fun getArticles(): LiveData<MutableList<HomeArticleDetail>?> = liveData {
        val result = repository.getArticles()
        emit(result)
    }




    fun getHomeArticles() = Pager(constPagerConfig) {
        HomeArticlePagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getHomeArticlesByRoomCache() = Pager(
        constPagerConfig,
        remoteMediator = HomeArtRemoteMediator(repository, repository.db),
        pagingSourceFactory = repository.pagingSourceFactory
    ).flow.cachedIn(viewModelScope)
}