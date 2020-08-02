package com.kuky.demo.wan.android.ui.hotproject

import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import com.kuky.demo.wan.android.entity.ProjectCategoryData
import com.kuky.demo.wan.android.entity.ProjectDetailData

/**
 * @author kuky.
 * @description
 */
class HotProjectPagingSource(
    private val repository: HotProjectRepository, private val pid: Int
) : PagingSource<Int, ProjectDetailData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProjectDetailData> {
        val page = params.key ?: 0

        return try {
            val projects = repository.loadProjects(page, pid) ?: mutableListOf()

            return LoadResult.Page(
                data = projects,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (projects.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


class CategoryDiffCall(
    private val oldList: MutableList<ProjectCategoryData>?,
    private val newList: MutableList<ProjectCategoryData>?
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldList.isNullOrEmpty() || newList.isNullOrEmpty()) false
        else oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldList.isNullOrEmpty() || newList.isNullOrEmpty()) false
        else oldList[oldItemPosition].name == newList[newItemPosition].name
}