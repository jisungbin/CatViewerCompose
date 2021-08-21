package io.github.jisungbin.catviewercompose.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.jisungbin.catviewercompose.util.StringListConverter

@TypeConverters(StringListConverter::class)
@Database(entities = [CatEntity::class], version = 1)
abstract class CatDatabase : RoomDatabase() {
    abstract fun dao(): CatDao

    companion object {
        fun build(context: Context) = synchronized(CatDatabase::class.java) {
            Room.databaseBuilder(context, CatDatabase::class.java, "cats.db")
                .fallbackToDestructiveMigration().build()
        }
    }
}
