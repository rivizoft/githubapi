package com.shiryaev.githubapi.presentation

import androidx.fragment.app.Fragment
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.presentation.fragments.AuthFragment
import com.shiryaev.githubapi.presentation.fragments.MainFragment
import com.shiryaev.githubapi.presentation.fragments.RepositoryInfoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens
{
    class MainScreen : SupportAppScreen()
    {
        override fun getFragment(): Fragment? {
            return MainFragment()
        }
    }

    class AuthScreen : SupportAppScreen()
    {
        override fun getFragment(): Fragment? {
            return AuthFragment()
        }
    }

    class RepositoryInfo(private val repositoryData: RepositoryData) : SupportAppScreen()
    {
        override fun getFragment(): Fragment? {
            return RepositoryInfoFragment.newInstance(repositoryData)
        }
    }
}