package com.shiryaev.githubapi.presentation.views

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface OAuthView : MvpView
{
    fun startAuthorization()
    fun finishAuthorization()
    fun showError(message: String)
    fun showError(message: Int)
}