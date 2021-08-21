package io.github.jisungbin.catviewercompose.util

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun listToString(list: List<String>) = list.joinToString(",")

    @TypeConverter
    fun stringToList(string: String) = string.split(",")
}
