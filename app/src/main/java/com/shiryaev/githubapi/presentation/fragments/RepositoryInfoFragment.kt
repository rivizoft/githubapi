package com.shiryaev.githubapi.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.presentation.di.DI
import com.shiryaev.githubapi.presentation.presenters.FavoritePresenter
import com.shiryaev.githubapi.presentation.presenters.RepositoryInfoPresenter
import com.shiryaev.githubapi.presentation.views.ListView
import com.shiryaev.githubapi.presentation.views.RepositoryInfoView
import kotlinx.android.synthetic.main.fragment_repository_info.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.text.SimpleDateFormat

class RepositoryInfoFragment : MvpAppCompatFragment(), RepositoryInfoView
{
    @InjectPresenter
    lateinit var favoritePresenter: RepositoryInfoPresenter

    @ProvidePresenter
    fun provideFavoritePresenter(): RepositoryInfoPresenter = Toothpick.openScope(DI.ROOT_SCOPE)
        .getInstance(RepositoryInfoPresenter::class.java)

    private var currentRepositoryData: RepositoryData? = null

    companion object
    {
        fun newInstance(repositoryData: RepositoryData): RepositoryInfoFragment
        {
            val fragment = RepositoryInfoFragment()

            val args = Bundle().apply {
                putSerializable("repositoryData", repositoryData)
            }

            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {

        val view = inflater.inflate(R.layout.fragment_repository_info, container, false)

        if (arguments != null)
        {
            currentRepositoryData = requireArguments().getSerializable("repositoryData") as RepositoryData
            view.tv_repo_name.text = currentRepositoryData!!.title
            view.tv_profile_login.text = currentRepositoryData!!.authorLogin

            val forks = currentRepositoryData!!.forkCount
            val stars = currentRepositoryData!!.starCount

            if (forks >= 1000)
                view.tv_count_forks_info.text = "${String.format("%.1f", forks.toDouble() / 1000)}k"
            else
                view.tv_count_forks_info.text = forks.toString()

            if (stars >= 1000)
                view.tv_count_stars_info.text = "${String.format("%.1f", stars.toDouble() / 1000)}k"
            else
                view.tv_count_stars_info.text = stars.toString()

            view.tv_date_info.text = currentRepositoryData!!.date

            view.tv_description_info.text = currentRepositoryData!!.description

            //Glide
            Glide.with(this)
                .load(currentRepositoryData!!.authorAvatarUrl)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(view.iv_profile_avatar)
        }

        view.toggle_button_add_remove_repo_db.isChecked = (currentRepositoryData != null &&
                favoritePresenter.existRepositoryInFavorite(currentRepositoryData!!))

        view.toggle_button_add_remove_repo_db.setOnCheckedChangeListener { buttonView, isChecked ->
            if (currentRepositoryData != null)
                if (isChecked)
                    favoritePresenter.addToFavorite(currentRepositoryData!!)
                else
                    favoritePresenter.deleteFromFavorite(currentRepositoryData!!)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritePresenter.checkUser()
    }

    override fun hideButtons()
    {
        requireView().toggle_button_add_remove_repo_db.visibility = View.GONE
    }

    override fun showButtons()
    {
        requireView().toggle_button_add_remove_repo_db.visibility = View.VISIBLE
    }

    override fun showMessage(message: String)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: Int)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
