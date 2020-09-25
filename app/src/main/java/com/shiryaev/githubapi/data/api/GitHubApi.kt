package com.shiryaev.githubapi.data.api

import com.shiryaev.githubapi.domain.RepositoryData
import com.shiryaev.githubapi.domain.User
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface GitHubApi
{
    @GET("user")
    fun getUserInfo(): Call<User>

    @GET("search/repositories")
    fun getRepositories(@Query("q") nameRepository: String,
                        @Query("page") page: Int,
                        @Query("sort") sort: String = "stars"): Call<Array<RepositoryData>>
}