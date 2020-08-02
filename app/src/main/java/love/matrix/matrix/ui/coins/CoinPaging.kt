package com.kuky.demo.wan.android.ui.coins


import androidx.paging.PagingSource
import com.kuky.demo.wan.android.entity.CoinRankDetail
import com.kuky.demo.wan.android.entity.CoinRecordDetail

/**
 * @author kuky.
 * @description
 */
class CoinRecordPagingSource(
    private val repository: CoinRepository
) : PagingSource<Int, CoinRecordDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinRecordDetail> {
        val page = params.key ?: 1

        return try {
            val records = repository.getCoinRecord(page)
            LoadResult.Page(
                data = records.datas,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (records.pageCount == page) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}



class CoinRankPagingSource(
    private val repository: CoinRepository
) : PagingSource<Int, CoinRankDetail>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinRankDetail> {
        val page = params.key ?: 1

        return try {
            val ranks = repository.getCoinRanks(page)

            LoadResult.Page(
                data = ranks.datas,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (ranks.pageCount == page) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

