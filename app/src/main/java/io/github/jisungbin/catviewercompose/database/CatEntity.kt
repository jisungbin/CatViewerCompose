package io.github.jisungbin.catviewercompose.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrls: List<String>
)
