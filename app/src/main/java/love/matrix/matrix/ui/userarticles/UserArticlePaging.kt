package com.kuky.demo.wan.android.ui.userarticles

import androidx.paging.PagingSource
import com.kuky.demo.wan.android.entity.UserArticleDetail

/**
 * @author kuky.
 * @description
 */

class UserArticlePagingSource(
    private val repository: UserArticleRepository
) : PagingSource<Int, UserArticleDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserArticleDetail> {
        val page = params.key ?: 0

        return try {
            val articles = repository.fetchUserArticles(page) ?: mutableListOf()

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

