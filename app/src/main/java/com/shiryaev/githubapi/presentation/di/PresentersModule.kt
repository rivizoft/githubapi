package com.shiryaev.githubapi.presentation.di

import com.shiryaev.githubapi.presentation.presenters.FavoritePresenter
import com.shiryaev.githubapi.presentation.presenters.OAuthPresenter
import com.shiryaev.githubapi.presentation.presenters.RepositoryInfoPresenter
import com.shiryaev.githubapi.presentation.presenters.SearchPresenter
import toothpick.config.Module

class PresentersModule : Module()
{
    init
    {
        bind(SearchPresenter::class.java)
        bind(OAuthPresenter::class.java)
        bind(FavoritePresenter::class.java)
        bind(RepositoryInfoPresenter::class.java)
    }
}