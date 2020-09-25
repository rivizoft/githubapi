package com.shiryaev.githubapi.presentation.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.presentation.adapters.PageAdapter
import com.shiryaev.githubapi.presentation.di.DI
import com.shiryaev.githubapi.presentation.presenters.OAuthPresenter
import com.shiryaev.githubapi.presentation.views.OAuthView
import kotlinx.android.synthetic.main.fragment_main.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

class MainFragment : MvpAppCompatFragment(), OAuthView {

    @InjectPresenter
    lateinit var oAuthPresenter: OAuthPresenter

    @ProvidePresenter
    fun provideOAuthPresenter(): OAuthPresenter = Toothpick.openScope(DI.ROOT_SCOPE)
        .getInstance(OAuthPresenter::class.java)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        //Setting Tool Bar Menu
        requireActivity().setActionBar(view.main_toolbar)
        setHasOptionsMenu(true)

        val viewPagerAdapter = PageAdapter(childFragmentManager).apply {
            addFragment(SearchFragment(), "Поиск")
            if (oAuthPresenter.getCurrentUser() != null)
                addFragment(FavoriteFragment(), "Избранное")
        }

        view.main_view_pager.adapter = viewPagerAdapter
        view.main_tab_layout.setupWithViewPager(view.main_view_pager)

        if (oAuthPresenter.getCurrentUser() != null)
            view.main_toolbar.title = oAuthPresenter.getCurrentUser()!!.name
        else
            view.main_toolbar.title = "Гость"

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                oAuthPresenter.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun startAuthorization() {
        return
    }

    override fun finishAuthorization() {
        return
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
