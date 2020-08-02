package com.kuky.demo.wan.android.ui.collectedarticles

import androidx.paging.PagingSource
import com.kuky.demo.wan.android.entity.UserCollectDetail


/**
 * @author kuky.
 * @description
 */
class CollectedArticlesPagingSource(
    private val repository: CollectedArticlesRepository
) : PagingSource<Int, UserCollectDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserCollectDetail> {
        val page = params.key ?: 0

        return try {
            val collectedArticles = repository.getCollectedArticleList(page) ?: mutableListOf()

            LoadResult.Page(
                data = collectedArticles,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (collectedArticles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
