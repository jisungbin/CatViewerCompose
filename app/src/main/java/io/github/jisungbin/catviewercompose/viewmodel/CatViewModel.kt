package io.github.jisungbin.catviewercompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jisungbin.catviewercompose.api.CatService
import io.github.jisungbin.catviewercompose.paging.CatPagingSource
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(private val api: CatService) : ViewModel() {
    var pagingLimit = 10

    val catPager = Pager(
        config = PagingConfig(
            pageSize = pagingLimit,
            enablePlaceholders = false,
            maxSize = 100
        ),
        pagingSourceFactory = { CatPagingSource(api, pagingLimit) }
    ).flow.cachedIn(viewModelScope)
}
