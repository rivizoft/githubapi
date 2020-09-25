package com.shiryaev.githubapi.presentation.di

import com.shiryaev.githubapi.data.repositoriesImpl.UserRepositoryImpl
import com.shiryaev.githubapi.domain.repositories.UserRepository
import com.shiryaev.githubapi.domain.usecases.UserUseCase
import toothpick.config.Module

class UserModule : Module()
{
    init
    {
        bind(UserRepository::class.java).to(UserRepositoryImpl::class.java).singleton()
        bind(UserUseCase::class.java)
    }
}