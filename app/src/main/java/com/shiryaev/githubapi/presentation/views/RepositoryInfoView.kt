package com.shiryaev.githubapi.presentation.views

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface RepositoryInfoView : MvpView
{
    fun hideButtons()
    fun showButtons()
    fun showMessage(message: String)
    fun showMessage(message: Int)
}