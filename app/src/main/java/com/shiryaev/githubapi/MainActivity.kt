package com.shiryaev.githubapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.shiryaev.githubapi.presentation.Screens
import com.shiryaev.githubapi.presentation.di.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : FragmentActivity()
{
    private val navigator: Navigator = object : SupportAppNavigator(this, R.id.fragment_main_container)
    {
        override fun applyCommands(commands: Array<out Command>) {
            super.applyCommands(commands)
            supportFragmentManager.executePendingTransactions()
        }
    }

    @Inject
    lateinit var cicerone: Cicerone<Router>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootScope = Toothpick.openScope("ROOT")
        rootScope.installModules(ContextModule(this), OAuthModule(), UserModule(),
            NavigationModule(), ClientsModule(), PresentersModule(), DatabaseModule(applicationContext))
        rootScope.inject(this)

        navigator.applyCommands(arrayOf(Replace(Screens.AuthScreen())))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        supportFragmentManager.fragments.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResumeFragments()
    {
        super.onResumeFragments()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause()
    {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }
}
