package io.github.jisungbin.catviewercompose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jisungbin.catviewercompose.api.CatResult
import io.github.jisungbin.catviewercompose.api.CatService
import io.github.jisungbin.catviewercompose.api.NetworkException
import io.github.jisungbin.catviewercompose.database.CatDatabase
import io.github.jisungbin.catviewercompose.database.CatEntity
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(
    private val api: CatService,
    private val database: CatDatabase
) : ViewModel() {
    private val _cat = mutableStateOf<CatResult>(CatResult.Loading)
    val cat: State<CatResult> get() = _cat

    init {
        loadCats(0)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun loadCats(page: Int, limit: Int = 10) = viewModelScope.launch {
        _cat.value = CatResult.Loading
        try {
            val request = api.requestCats(page, limit)

            if (request.isSuccessful && request.body() != null) {
                _cat.value = CatResult.Success(request.body()!!.string().parseCatImages(limit))
            } else {
                _cat.value = CatResult.Fail(Exception(request.errorBody()?.string()))
            }
        } catch (exception: Exception) {
            _cat.value = CatResult.Fail(NetworkException())
        }
    }

    fun insertAllToDatabase(catImageUrls: List<String>) = viewModelScope.launch {
        database.dao().insert(CatEntity(imageUrls = catImageUrls))
    }

    fun loadImageUrlsFromDatabase() = viewModelScope.launch {
        val catImageUrls = database.dao().getAll().map { it.imageUrls }.flatten()
        if (catImageUrls.isNotEmpty()) {
            _cat.value = CatResult.Success(catImageUrls)
        } else {
            _cat.value = CatResult.Fail(Exception("인터넷 연결이 필요합니다."))
        }
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
