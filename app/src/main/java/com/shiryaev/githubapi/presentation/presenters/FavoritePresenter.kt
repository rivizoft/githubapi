package com.shiryaev.githubapi.presentation.presenters

import androidx.lifecycle.asLiveData
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

@InjectConstructor
@InjectViewState
class FavoritePresenter(private val dataRepositoryUseCase: RepositoriesDataUseCase,
                        private val router: Router) : MvpPresenter<ListView>()
{
    fun showFeaturedRepositories()
    {
        dataRepositoryUseCase.getRepositoriesFromLocalDB().asLiveData().observeForever {
            viewState.showResults(it)
        }
    }

    fun showFavoriteRepositoriesWithTitleLike(title: String)
    {
        dataRepositoryUseCase.getAllRepositoriesWithTitleLike(title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showProgressBar() }
            .doOnTerminate { viewState.hideProgressBar() }
            .subscribe {
                it -> viewState.showResults(it)
            }
    }

    fun openFavoriteRepository(repositoryData: RepositoryData)
    {
        router.navigateTo(Screens.RepositoryInfo(repositoryData))
    }

    fun deleteAllFavoriteRepositories()
    {
        dataRepositoryUseCase.deleteAllRepositories()
    }
}