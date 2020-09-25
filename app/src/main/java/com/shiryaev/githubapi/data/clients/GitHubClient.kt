package com.shiryaev.githubapi.data.clients

import com.google.gson.GsonBuilder
import com.shiryaev.githubapi.data.interceptors.CustomInterceptor
import com.shiryaev.githubapi.data.api.GitHubApi
import com.shiryaev.githubapi.data.deserializers.GithubRepositoryDeserializer
import com.shiryaev.githubapi.data.deserializers.UserDeserializer
import com.shiryaev.githubapi.domain.*
import com.shiryaev.githubapi.domain.constants.ApiConstants
import com.shiryaev.githubapi.domain.repositories.UserRepository
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import toothpick.InjectConstructor
import java.net.CookieManager
import java.net.CookiePolicy

@InjectConstructor
class GitHubClient(private val userRepository: UserRepository)
{
    var accessToken: AccessToken? = userRepository.currentUser?.accessToken

    private val cookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)

    private val client by lazy()
    {
        OkHttpClient.Builder()
            .addInterceptor(CustomInterceptor(accessToken))
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(User::class.java, UserDeserializer())
        .registerTypeAdapter(Array<RepositoryData>::class.java, GithubRepositoryDeserializer())
        .create()

    private val retrofit by lazy()
    {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_API_GITHUB_URL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api by lazy()
    {
        retrofit.create(GitHubApi::class.java)
    }
}