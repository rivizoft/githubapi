package com.shiryaev.githubapi.presentation.di

import android.content.Context
import androidx.room.Room
import com.shiryaev.githubapi.data.db.RepositoryDataDatabase
import toothpick.config.Module

class DatabaseModule(context: Context) : Module()
{
    init
    {
        val db = Room
            .databaseBuilder(context, RepositoryDataDatabase::class.java, "repositories_database")
            .allowMainThreadQueries()
            .build()
        bind(RepositoryDataDatabase::class.java).toInstance(db)
    }
}