package com.kuky.demo.wan.android.ui.home

import androidx.paging.PagingSource
import com.kuky.demo.wan.android.data.db.HomeArticleDetail

/**
 * @author kuky.
 * @description
 */
class HomeArticlePagingSource(
    private val repository: HomeArticleRepository
) : PagingSource<Int, HomeArticleDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleDetail> {
        val page = params.key ?: 0
        return try {
            val article = if (page == 0) mutableListOf<HomeArticleDetail>().apply {
                addAll(repository.loadTops() ?: mutableListOf())
                addAll(repository.loadPageData(page) ?: mutableListOf())
            } else (repository.loadPageData(page) ?: mutableListOf())

            LoadResult.Page(
                data = article,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (article.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}

