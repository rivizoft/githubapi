package com.shiryaev.githubapi.presentation.presenters

import android.content.Intent
import com.shiryaev.githubapi.data.clients.GoogleAuthClient
import com.shiryaev.githubapi.domain.constants.Auth
import com.shiryaev.githubapi.domain.usecases.OAuthUseCase
import com.shiryaev.githubapi.domain.User
import com.shiryaev.githubapi.domain.usecases.UserUseCase
import com.shiryaev.githubapi.presentation.Screens
import com.shiryaev.githubapi.presentation.views.OAuthView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor

@InjectViewState
@InjectConstructor
class OAuthPresenter(private val oAuthUseCase: OAuthUseCase,
                     private val userUseCase: UserUseCase,
                     private val googleAuthClient: GoogleAuthClient,
                     private val router: Router) : MvpPresenter<OAuthView>()
{

    fun signIn(authType: Auth.Type)
    {
        when (authType)
        {
            Auth.Type.Google -> googleAuthClient.signIn()
            Auth.Type.OAuth -> oAuthUseCase.signIn()
            Auth.Type.Guest -> router.replaceScreen(Screens.MainScreen())
        }
    }

    fun signOut()
    {
        oAuthUseCase.signOut()
        googleAuthClient.signOut()
        userUseCase.setCurrentUser(null)
        GlobalScope.launch {
            withContext(Dispatchers.Main)
            {
                router.replaceScreen(Screens.AuthScreen())
            }
        }
    }

    fun checkUserAuth()
    {
        if (userUseCase.existLoggedUser())
            router.replaceScreen(Screens.MainScreen())
    }

    fun getCurrentUser(): User? = userUseCase.getCurrentUser()

    fun createUser(code: String)
    {
        viewState.startAuthorization()
        oAuthUseCase.getUserInfo(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                viewState.finishAuthorization()
                userUseCase.setCurrentUser(success)
                router.replaceScreen(Screens.MainScreen())
            }, { error ->
                viewState.showError("Не удалось авторизоваться через GitHub")
                viewState.finishAuthorization()
            })
    }

    fun createUser(intent: Intent)
    {
        googleAuthClient.getUserInfo(intent)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                userUseCase.setCurrentUser(success)
                router.replaceScreen(Screens.MainScreen())
            }, { error ->
                viewState.showError("Не удалось авторизоваться через Google")
                viewState.finishAuthorization()
            })
    }
}