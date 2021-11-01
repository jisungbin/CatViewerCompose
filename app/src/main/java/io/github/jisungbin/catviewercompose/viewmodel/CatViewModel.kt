package io.github.jisungbin.catviewercompose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jisungbin.catviewercompose.api.CatClient
import io.github.jisungbin.catviewercompose.model.Cat
import kotlinx.coroutines.launch

class CatViewModel : ViewModel() {

    private val _cat = mutableStateOf<Cat?>(null)
    val cat: State<Cat?> get() = _cat

    init {
        reloadCat()
    }

    fun reloadCat() = viewModelScope.launch {
        _cat.value = CatClient.get.requestNewCat().body()
    }
}
