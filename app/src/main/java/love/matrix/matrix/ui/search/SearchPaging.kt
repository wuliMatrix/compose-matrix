package com.kuky.demo.wan.android.ui.search

import androidx.paging.PagingSource
import com.kuky.demo.wan.android.entity.ArticleDetail

/**
 * @author kuky.
 * @description
 */
class SearchPagingSource(
    private val repository: SearchRepository, private val key: String
) : PagingSource<Int, ArticleDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDetail> {
        val page = params.key ?: 0

        return try {
            val articles = repository.loadSearchResult(page, key) ?: mutableListOf()
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

