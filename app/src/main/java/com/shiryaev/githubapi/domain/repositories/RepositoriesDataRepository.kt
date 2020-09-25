package com.shiryaev.githubapi.domain.repositories

import com.shiryaev.githubapi.domain.RepositoryData
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface RepositoriesDataRepository
{
    fun getRepositoriesByNameFromGitHub(name: String, page: Int): Single<List<RepositoryData>>
    fun getRepositoriesFromLocalDB(): Flow<List<RepositoryData>>
    fun getRepositoriesWithTitleLike(title: String): List<RepositoryData>
    fun addRepositoryToLocalDB(repo: RepositoryData)
    fun deleteRepositoryFromLocalDB(repo: RepositoryData)
    fun countInDB(repo: RepositoryData): Int
    fun deleteAllRepositoriesFromBD()
}