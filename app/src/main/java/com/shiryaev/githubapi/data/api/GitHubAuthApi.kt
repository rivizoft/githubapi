package com.shiryaev.githubapi.data.api

import com.shiryaev.githubapi.domain.AccessToken
import com.shiryaev.githubapi.domain.constants.ApiConstants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GitHubAuthApi
{
    @Headers("Accept: application/json")
    @POST(ApiConstants.ACCESS_TOKEN_GITHUB_URL)
    @FormUrlEncoded
    fun getAccessToken (
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): Single<AccessToken?>
}