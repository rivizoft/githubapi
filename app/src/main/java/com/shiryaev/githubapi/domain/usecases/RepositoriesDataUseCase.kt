package com.shiryaev.githubapi.domain.usecases

import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.domain.repositories.RepositoriesDataRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import toothpick.InjectConstructor

@InjectConstructor
class RepositoriesDataUseCase(private val repositoriesDataRepository: RepositoriesDataRepository)
{
    fun getRepositories(name: String, page: Int): Single<List<RepositoryData>>
    {
        return repositoriesDataRepository.getRepositoriesByNameFromGitHub(name, page)
    }

    fun addRepositoryToLocalDB(repo: RepositoryData)
    {
        repositoriesDataRepository.addRepositoryToLocalDB(repo)
    }

    fun deleteRepositoryFromLocalDB(repo: RepositoryData)
    {
        repositoriesDataRepository.deleteRepositoryFromLocalDB(repo)
    }

    fun getRepositoriesFromLocalDB(): Flow<List<RepositoryData>>
    {
        return repositoriesDataRepository.getRepositoriesFromLocalDB()
    }

    fun existRepositoryInDB(repo: RepositoryData): Boolean
    {
        return repositoriesDataRepository.countInDB(repo) != 0
    }

    fun getAllRepositoriesWithTitleLike(title: String): Single<List<RepositoryData>>
    {
        return Single.create {
            it.onSuccess(repositoriesDataRepository.getRepositoriesWithTitleLike(title))
        }
    }

    fun deleteAllRepositories()
    {
        repositoriesDataRepository.deleteAllRepositoriesFromBD()
    }
}