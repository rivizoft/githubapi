package com.shiryaev.githubapi.data.clients

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.shiryaev.githubapi.data.api.GitHubAuthApi
import com.shiryaev.githubapi.domain.AccessToken
import com.shiryaev.githubapi.domain.constants.ApiConstants
import com.shiryaev.githubapi.domain.User
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import toothpick.InjectConstructor
import java.net.CookieManager
import java.net.CookiePolicy

@InjectConstructor
class GitHubAuthClient(private val context: Context, private val gitHubClient: GitHubClient)
{
    private val CLIENT_PARAM = "client_id"
    private val SCOPE_PARAM = "scope"
    private val REDIRECT_URI_PARAM = "redirect_uri"

    private val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)

    private val client = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    private val retrofit by lazy()
    {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_GITHUB_URL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api by lazy()
    {
        retrofit.create(GitHubAuthApi::class.java)
    }

    fun signIn()
    {
        val uri = Uri.parse(ApiConstants.BASE_GITHUB_URL + ApiConstants.OAUTH_GITHUB_URL)
            .buildUpon()
            .appendQueryParameter(CLIENT_PARAM, ApiConstants.GITHUB_CLIENT_ID)
            .appendQueryParameter(SCOPE_PARAM, "repo")
            .appendQueryParameter(REDIRECT_URI_PARAM, ApiConstants.AUTH_CALLBACK_URL)
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(context, intent, null)
    }

    fun getUserInfo(code: String): Single<User>
    {
        return Single.create { subscriber ->
            getAccessToken(code)
                .subscribeOn(Schedulers.io())
                .subscribe { token ->
                    gitHubClient.accessToken = token
                    val tes = gitHubClient.api.getUserInfo().execute()
                    val user = tes.body()
                    if (user != null) {
                        user.accessToken = token
                        subscriber.onSuccess(user)
                    }
                    else
                        subscriber.onError(Throwable("User is null"))
            }
        }
    }

    fun getAccessToken(code: String): Single<AccessToken?>
    {
        return api.getAccessToken(
            ApiConstants.GITHUB_CLIENT_ID,
            ApiConstants.GITHUB_CLIENT_SECRET, code)
    }
}