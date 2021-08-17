package io.github.jisungbin.catviewercompose.model

import com.google.gson.annotations.SerializedName

data class Cat(
    @field:SerializedName("created_at")
    val createdAt: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("url")
    val url: String = "",

    @field:SerializedName("tags")
    val tags: List<String> = emptyList()
)
