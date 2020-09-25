package com.shiryaev.githubapi.presentation.di

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class NavigationModule : Module()
{
    init
    {
        val instance: Cicerone<Router> = Cicerone.create()
        bind(Cicerone::class.java).toInstance(instance)
        bind(Router::class.java).toInstance(instance.router)
    }
}