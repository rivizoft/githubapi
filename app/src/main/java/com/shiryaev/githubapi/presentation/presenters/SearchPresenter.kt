package com.shiryaev.githubapi.presentation.presenters

import com.shiryaev.githubapi.domain.usecases.RepositoriesDataUseCase
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.presentation.Screens
import com.shiryaev.githubapi.presentation.views.ListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor

@InjectViewState
@InjectConstructor
class SearchPresenter(private val repositoriesDataUseCase: RepositoriesDataUseCase,
                      private val router: Router) :
    MvpPresenter<ListView>()
{
    private var pageCount = 1
    private var lastQueryName: String? = null
    private val listRepositories: MutableList<RepositoryData> = mutableListOf()
    private var isLoading = false

    fun showRepositoryInfo(repositoryData: RepositoryData)
    {
        router.navigateTo(Screens.RepositoryInfo(repositoryData))
    }

    fun showRepositoriesByNameFromGitHub(name: String)
    {
        if (isLoading)
            return

        if (name.isEmpty())
        {
            listRepositories.clear()
            viewState.showResults(listRepositories)
            return
        }

        if (name == lastQueryName)
            pageCount++
        else
        {
            listRepositories.clear()
            pageCount = 1
        }

        repositoriesDataUseCase.getRepositories(name, pageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.showProgressBar()
                isLoading = true
            }
            .doOnTerminate {
                viewState.hideProgressBar()
                isLoading = false
            }
            .subscribe({ success ->
                if (success.isNotEmpty())
                {
                    listRepositories.addAll(success)
                    viewState.showResults(listRepositories)
                }
                else
                    viewState.showToastMessage("Репозиториев с таким названием не существует!")
            }, { error ->
                viewState.showToastMessage(error.localizedMessage)
            })

        lastQueryName = name
    }
}