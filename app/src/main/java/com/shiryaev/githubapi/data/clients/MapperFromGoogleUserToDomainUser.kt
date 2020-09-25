package com.shiryaev.githubapi.data.clients

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.shiryaev.githubapi.domain.AccessToken
import com.shiryaev.githubapi.domain.User

object MapperFromGoogleUserToDomainUser
{
    fun mapFromGoogleUserToDomainUser(googleUser: GoogleSignInAccount): User
    {
        return User(
            null,
            googleUser.displayName ?: "Неизвестный",
            if (googleUser.idToken != null)
                AccessToken(googleUser.idToken!!, "google")
            else
                null,
            null
        )
    }
}