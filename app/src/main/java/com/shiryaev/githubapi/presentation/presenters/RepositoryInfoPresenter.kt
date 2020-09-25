package com.shiryaev.githubapi.presentation.presenters

import com.shiryaev.githubapi.domain.usecases.RepositoriesDataUseCase
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.domain.usecases.UserUseCase
import com.shiryaev.githubapi.presentation.views.RepositoryInfoView
import moxy.InjectViewState
import moxy.MvpPresenter
import toothpick.InjectConstructor

@InjectConstructor
@InjectViewState
class RepositoryInfoPresenter(private val userUseCase: UserUseCase,
                              private val repoUseCase: RepositoriesDataUseCase
)
    : MvpPresenter<RepositoryInfoView>()
{
    fun addToFavorite(repos: RepositoryData)
    {
        repoUseCase.addRepositoryToLocalDB(repos)
        viewState.showMessage("Репозиторий добавлен в избранное")
    }

    fun deleteFromFavorite(repos: RepositoryData)
    {
        repoUseCase.deleteRepositoryFromLocalDB(repos)
        viewState.showMessage("Репозиторий удален из избранного")
    }

    fun existRepositoryInFavorite(repos: RepositoryData): Boolean
    {
        return repoUseCase.existRepositoryInDB(repos)
    }

    fun checkUser()
    {
        if (userUseCase.getCurrentUser() == null)
            viewState.hideButtons()
        else
            viewState.showButtons()
    }
}