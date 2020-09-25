package com.shiryaev.githubapi.presentation.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.domain.constants.Auth
import com.shiryaev.githubapi.presentation.di.DI
import com.shiryaev.githubapi.presentation.presenters.OAuthPresenter
import com.shiryaev.githubapi.presentation.views.OAuthView
import kotlinx.android.synthetic.main.fragment_auth.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

class AuthFragment : MvpAppCompatFragment(), OAuthView
{
    @InjectPresenter
    lateinit var oAuthPresenter: OAuthPresenter

    @ProvidePresenter
    fun provideOAuthPresenter(): OAuthPresenter = Toothpick.openScope(DI.ROOT_SCOPE)
        .getInstance(OAuthPresenter::class.java)

    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        //Create Progress Dialog
        progressDialog = createProgressDialog()

        //Button Github
        view.button_signIn_github.setOnClickListener {
            oAuthPresenter.signIn(Auth.Type.OAuth)
        }

        //Button Google
        view.button_signIn_google.setOnClickListener {
            oAuthPresenter.signIn(Auth.Type.Google)
        }

        //Button as Guest
        view.button_signIn_guest.setOnClickListener {
            oAuthPresenter.signIn(Auth.Type.Guest)

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Check User
        oAuthPresenter.checkUserAuth()
    }

    private fun checkInputDataFromBrowser()
    {
        val uri: Uri? = requireActivity().intent.data
        if (uri != null)
        {
            val code = uri.getQueryParameter("code") as String
            oAuthPresenter.createUser(code)
            requireActivity().intent.data = null
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null)
            oAuthPresenter.createUser(data)
    }

    override fun onResume()
    {
        super.onResume()
        checkInputDataFromBrowser()
    }

    private fun createProgressDialog(): AlertDialog
    {
        return AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(R.layout.loading_dialog)
            .create()
    }

    override fun startAuthorization()
    {
        progressDialog.show()
    }

    override fun finishAuthorization()
    {
        progressDialog.hide()
    }

    override fun showError(message: String)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: Int)
    {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
