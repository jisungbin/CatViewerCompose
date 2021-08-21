package io.github.jisungbin.catviewercompose.api

sealed class CatResult {
    object Loading : CatResult()
    data class Success(val catImageUrls: List<String>) : CatResult()
    data class Fail(val exception: Exception) : CatResult()
}
