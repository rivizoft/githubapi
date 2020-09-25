package com.shiryaev.githubapi.data.repositoriesImpl

import com.shiryaev.githubapi.data.clients.GitHubClient
import com.shiryaev.githubapi.data.db.RepositoryDataDatabase
import com.shiryaev.githubapi.domain.repositories.RepositoriesDataRepository
import com.shiryaev.githubapi.domain.RepositoryData
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor

@InjectConstructor
class RepositoriesDataRepositoryImpl(private val gitHubClient: GitHubClient,
                                     private val db: RepositoryDataDatabase) :
    RepositoriesDataRepository
{
    override fun getRepositoriesByNameFromGitHub(name: String, page: Int): Single<List<RepositoryData>>
    {
        val query = "$name in:name"
        return Single.create {
            val result = gitHubClient.api.getRepositories(query, page).execute().body()
            if (result != null)
            {
                it.onSuccess(result.toList())
            }
            else
                it.onError(Throwable("API rate limit exceeded"))
        }
    }

    override fun getRepositoriesFromLocalDB(): Flow<List<RepositoryData>>
    {
        return db.dao().getAllRepositories()
    }

    override fun getRepositoriesWithTitleLike(title: String): List<RepositoryData>
    {
        return db.dao().getAllRepositoriesWithTitleLike(title)
    }

    override fun addRepositoryToLocalDB(repo: RepositoryData)
    {
        db.dao().insertRepository(repo)
    }

    override fun deleteRepositoryFromLocalDB(repo: RepositoryData)
    {
        db.dao().deleteRepository(repo)
    }

    override fun countInDB(repo: RepositoryData): Int
    {
        return db.dao().count(repo.id)
    }

    override fun deleteAllRepositoriesFromBD()
    {
        db.dao().deleteAllRepositories()
    }
}