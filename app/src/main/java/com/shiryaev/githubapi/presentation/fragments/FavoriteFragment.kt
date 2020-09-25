package com.shiryaev.githubapi.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.textChangeEvents

import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.presentation.adapters.ClickOnRepoListener
import com.shiryaev.githubapi.presentation.adapters.ReposListViewAdapter
import com.shiryaev.githubapi.presentation.di.DI
import com.shiryaev.githubapi.presentation.presenters.FavoritePresenter
import com.shiryaev.githubapi.presentation.views.ListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.util.concurrent.TimeUnit

class FavoriteFragment : MvpAppCompatFragment(), ListView {

    @InjectPresenter
    lateinit var favoritePresenter: FavoritePresenter

    @ProvidePresenter
    fun provideFavoritePresenter(): FavoritePresenter = Toothpick.openScope(DI.ROOT_SCOPE)
        .getInstance(FavoritePresenter::class.java)

    private lateinit var recyclerAdapter: ReposListViewAdapter
    private val savedState = Bundle()

    private val KEY_ADAPTER = "adapter"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        view.et_search_repos_favorite.textChangeEvents()
            .skip(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                favoritePresenter.showFavoriteRepositoriesWithTitleLike(it.text.toString())
            }

        recyclerAdapter = if (savedState.containsKey(KEY_ADAPTER))
            savedState.getSerializable(KEY_ADAPTER) as ReposListViewAdapter
        else
            ReposListViewAdapter(object : ClickOnRepoListener {
                override fun onRepoItemClicked(repo: RepositoryData, position: Int) {
                    favoritePresenter.openFavoriteRepository(repo)
                }
            })

        view.rv_results_repos_favorite.adapter = recyclerAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        view.rv_results_repos_favorite.layoutManager = layoutManager

        //setting delete button
        view.button_clear_favorite.setOnClickListener {
            favoritePresenter.deleteAllFavoriteRepositories()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        favoritePresenter.showFeaturedRepositories()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        savedState.putSerializable(KEY_ADAPTER, recyclerAdapter)
    }

    override fun showProgressBar()
    {
        requireView().progress_bar_results_favorite.visibility = View.VISIBLE
    }

    override fun hideProgressBar()
    {
        requireView().progress_bar_results_favorite.visibility = View.GONE
    }

    override fun showResults(items: List<RepositoryData>)
    {
        recyclerAdapter.setData(items)
    }

    override fun showToastMessage(message: String)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showToastMessage(message: Int)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
