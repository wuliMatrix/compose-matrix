package com.kuky.demo.wan.android.ui.todolist


import androidx.paging.PagingSource
import com.kuky.demo.wan.android.entity.TodoInfo

/**
 * @author kuky.
 * @description
 */
class TodoPagingSource(
    private val repository: TodoListRepository, private val param: HashMap<String, Int>
) : PagingSource<Int, TodoInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TodoInfo> {
        val page = params.key ?: 1

        return try {
            val todoList = repository.fetchTodoList(page, param) ?: mutableListOf()

            LoadResult.Page(
                data = todoList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (todoList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
