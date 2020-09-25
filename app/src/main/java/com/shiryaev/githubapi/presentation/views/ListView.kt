package com.shiryaev.githubapi.presentation.views

import com.shiryaev.githubapi.domain.RepositoryData
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface ListView : MvpView
{
    fun showProgressBar()
    fun hideProgressBar()
    fun showResults(items: List<RepositoryData>)
    fun showToastMessage(message: String)
    fun showToastMessage(message: Int)
}