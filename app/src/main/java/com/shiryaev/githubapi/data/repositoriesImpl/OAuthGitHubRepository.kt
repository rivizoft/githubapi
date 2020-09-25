package com.shiryaev.githubapi.data.repositoriesImpl

import com.shiryaev.githubapi.data.clients.GitHubAuthClient
import com.shiryaev.githubapi.domain.repositories.OAuthRepository
import com.shiryaev.githubapi.domain.User
import io.reactivex.rxjava3.core.Single
import toothpick.InjectConstructor

@InjectConstructor
class OAuthGitHubRepository(private val gitHubAuthClient: GitHubAuthClient) : OAuthRepository
{
    override fun signIn()
    {
        gitHubAuthClient.signIn()
    }

    override fun getUserInfo(code: String): Single<User>
    {
        return gitHubAuthClient.getUserInfo(code)
    }


    override fun signOut()
    {

    }

}