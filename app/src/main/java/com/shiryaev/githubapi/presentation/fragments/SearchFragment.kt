package com.shiryaev.githubapi.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChangeEvents

import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.presentation.adapters.ClickOnRepoListener
import com.shiryaev.githubapi.presentation.adapters.ReposListViewAdapter
import com.shiryaev.githubapi.presentation.di.DI
import com.shiryaev.githubapi.presentation.presenters.SearchPresenter
import com.shiryaev.githubapi.presentation.views.ListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.util.concurrent.TimeUnit

class SearchFragment : MvpAppCompatFragment(), ListView {

    @InjectPresenter
    lateinit var searchPresenter: SearchPresenter

    @ProvidePresenter
    fun provideSearchPresenter(): SearchPresenter = Toothpick.openScope(DI.ROOT_SCOPE)
        .getInstance(SearchPresenter::class.java)

    private lateinit var recyclerAdapter: ReposListViewAdapter
    private val savedState = Bundle()

    private val KEY_ADAPTER = "adapter"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        view.et_search_repos.textChangeEvents()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .skip(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchPresenter.showRepositoriesByNameFromGitHub(it.text.toString())
            }

        recyclerAdapter = if (savedState.containsKey(KEY_ADAPTER))
            savedState.getSerializable(KEY_ADAPTER) as ReposListViewAdapter
        else
            ReposListViewAdapter(object : ClickOnRepoListener {
                override fun onRepoItemClicked(repo: RepositoryData, position: Int) {
                    searchPresenter.showRepositoryInfo(repo)
                }
            })

        view.rv_results_repos.adapter = recyclerAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        view.rv_results_repos.layoutManager = layoutManager

        view.rv_results_repos.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                if (dy > 0)
                {
                    val item = layoutManager.childCount
                    val fItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val count = recyclerAdapter.itemCount

                    if (item + fItem >= count)
                    {
                        val name = view.et_search_repos.text.toString()
                        searchPresenter.showRepositoriesByNameFromGitHub(name)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        return view
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        savedState.putSerializable(KEY_ADAPTER, recyclerAdapter)
    }

    override fun showProgressBar()
    {
        requireView().progress_bar_results.visibility = View.VISIBLE
    }

    override fun hideProgressBar()
    {
        requireView().progress_bar_results.visibility = View.GONE
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
