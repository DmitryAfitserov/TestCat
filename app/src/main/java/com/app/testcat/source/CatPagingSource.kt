package com.app.testcat.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.testcat.R
import com.app.testcat.api.ApiCat
import com.app.testcat.app.CatApplication
import com.app.testcat.model.CatNet


class CatPagingSource(
    private val apiCat: ApiCat,
) : PagingSource<Int, CatNet>() {

    companion object {
        const val PAGE_SIZE = 10
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, CatNet>): Int {
        return 1
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatNet> {
        val page = params.key ?: STARTING_PAGE_INDEX
        try {

            val response = apiCat.getAllCats(PAGE_SIZE)

            if (response.isSuccessful) {

                val listCat = response.body()!!

                val prefKey = if (page == 1) null else page - 1
                val nextKey = page + 1
                return LoadResult.Page(listCat, prefKey, nextKey)

            } else {
                return LoadResult.Error(Throwable(CatApplication.instance.getString(R.string.error_loading)))
            }
        } catch (e: Exception) {
            return LoadResult.Error(Throwable(CatApplication.instance.getString(R.string.error_loading)))
        }
    }

}

