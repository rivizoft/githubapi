package com.shiryaev.githubapi.data.db

import androidx.room.*
import com.shiryaev.githubapi.domain.RepositoryData
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDataDao
{
    @Query("SELECT * FROM repositories_table")
    fun getAllRepositories(): Flow<List<RepositoryData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRepository(repositoryData: RepositoryData)

    @Delete
    fun deleteRepository(repositoryData: RepositoryData)

    @Query("SELECT COUNT() FROM repositories_table WHERE id = :id")
    fun count(id: Long): Int

    @Query("SELECT * FROM repositories_table WHERE title LIKE '%' || :title || '%'")
    fun getAllRepositoriesWithTitleLike(title: String): List<RepositoryData>

    @Query("DELETE FROM repositories_table")
    fun deleteAllRepositories()
}