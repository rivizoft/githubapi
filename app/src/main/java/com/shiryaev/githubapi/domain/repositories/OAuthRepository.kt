package com.shiryaev.githubapi.domain.repositories

import com.shiryaev.githubapi.domain.User
import io.reactivex.rxjava3.core.Single

interface OAuthRepository
{
    fun signIn()
    fun getUserInfo(code: String): Single<User>
    fun signOut()
}