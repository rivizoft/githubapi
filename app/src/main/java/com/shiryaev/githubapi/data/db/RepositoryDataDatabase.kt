package com.shiryaev.githubapi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shiryaev.githubapi.data.db.RepositoryDataDao
import com.shiryaev.githubapi.domain.RepositoryData
import toothpick.InjectConstructor

@Database(entities = [RepositoryData::class], version = 1, exportSchema = false)
abstract class RepositoryDataDatabase : RoomDatabase()
{
    abstract fun dao(): RepositoryDataDao
}