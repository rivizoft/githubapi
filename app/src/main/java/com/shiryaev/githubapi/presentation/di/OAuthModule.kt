package com.shiryaev.githubapi.presentation.di

import com.shiryaev.githubapi.data.repositoriesImpl.OAuthGitHubRepository
import com.shiryaev.githubapi.data.clients.GitHubAuthClient
import com.shiryaev.githubapi.data.clients.GitHubClient
import com.shiryaev.githubapi.data.clients.GoogleAuthClient
import com.shiryaev.githubapi.domain.repositories.OAuthRepository
import com.shiryaev.githubapi.domain.usecases.OAuthUseCase
import toothpick.config.Module

class OAuthModule() : Module()
{
    init
    {
        bind(OAuthUseCase::class.java)
        bind(OAuthRepository::class.java).to(OAuthGitHubRepository::class.java)

        bind(GoogleAuthClient::class.java).singleton()
        bind(GitHubAuthClient::class.java).singleton()
        bind(GitHubClient::class.java).singleton()
    }
}