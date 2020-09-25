package com.shiryaev.githubapi.presentation.di

import com.shiryaev.githubapi.data.repositoriesImpl.RepositoriesDataRepositoryImpl
import com.shiryaev.githubapi.domain.repositories.RepositoriesDataRepository
import com.shiryaev.githubapi.domain.usecases.RepositoriesDataUseCase
import toothpick.config.Module

class ClientsModule : Module()
{
    init
    {
        bind(RepositoriesDataRepository::class.java).to(RepositoriesDataRepositoryImpl::class.java)
        bind(RepositoriesDataUseCase::class.java)
    }
}