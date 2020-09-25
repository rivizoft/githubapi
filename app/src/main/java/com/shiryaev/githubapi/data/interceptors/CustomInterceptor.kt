package com.shiryaev.githubapi.data.interceptors

import com.shiryaev.githubapi.domain.AccessToken
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor(private val accessToken: AccessToken?) : Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response
    {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", " token ${accessToken?.accessToken}")
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}