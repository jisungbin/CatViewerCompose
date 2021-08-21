package io.github.jisungbin.catviewercompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catEntity: CatEntity)

    @Query("select * from CatEntity")
    suspend fun getAll(): List<CatEntity>
}
