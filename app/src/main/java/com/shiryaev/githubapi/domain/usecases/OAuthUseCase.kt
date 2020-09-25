package com.shiryaev.githubapi.domain.usecases

import com.shiryaev.githubapi.domain.User
import com.shiryaev.githubapi.domain.repositories.OAuthRepository
import io.reactivex.rxjava3.core.Single
import toothpick.InjectConstructor

@InjectConstructor
class OAuthUseCase(private val oAuthRepository: OAuthRepository)
{
    fun signIn()
    {
        oAuthRepository.signIn()
    }

    fun getUserInfo(code: String): Single<User>
    {
        return oAuthRepository.getUserInfo(code)
    }

    fun signOut()
    {
        oAuthRepository.signOut()
    }
}