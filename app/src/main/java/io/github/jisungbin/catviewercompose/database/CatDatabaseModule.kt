package io.github.jisungbin.catviewercompose.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jisungbin.catviewercompose.database.CatDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatDatabaseModule {
    @Singleton
    @Provides
    fun provideCatDatabase(@ApplicationContext context: Context) = CatDatabase.build(context)
}
