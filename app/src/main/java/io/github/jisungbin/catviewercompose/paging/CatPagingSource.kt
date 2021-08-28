package io.github.jisungbin.catviewercompose.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.jisungbin.catviewercompose.api.CatService
import org.json.JSONArray

class CatPagingSource(
    private val api: CatService,
    private val limit: Int,
) : PagingSource<Int, String>() {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val position = params.key ?: 0
            val response = api.requestCats(page = position, limit = params.loadSize)

            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.string().parseCatImages(limit)
                val nextKey = if (data.isEmpty()) {
                    null
                } else {
                    position + (params.loadSize / limit)
                }
                LoadResult.Page(
                    data = data,
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception(response.errorBody()?.string()))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        } // 현재 키 가져오기
    }

    private fun String.parseCatImages(limit: Int): List<String> {
        val catImageUrls = mutableListOf<String>()
        val catImages = JSONArray(this)
        repeat(limit) { index ->
            catImageUrls.add(catImages.getJSONObject(index).getString("url"))
        }
        return catImageUrls
    }
}
