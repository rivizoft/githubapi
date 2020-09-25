package com.shiryaev.githubapi.data.clients

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.shiryaev.githubapi.domain.AccessToken
import com.shiryaev.githubapi.domain.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import toothpick.InjectConstructor
import javax.inject.Singleton

@InjectConstructor
class GoogleAuthClient(private val activity: Activity)
{
    val client: GoogleSignInClient

    val REQUEST_SIGN_IN_CODE = 77

    init
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        client = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn()
    {
        val intent = client.signInIntent
        startActivityForResult(activity, intent, REQUEST_SIGN_IN_CODE, null)
    }

    fun signOut()
    {
        client.signOut()
    }

    fun existSignInUser(): Boolean = GoogleSignIn.getLastSignedInAccount(activity) != null

    fun getUserInfo(intent: Intent): Single<User>
    {
        return Single.create { emitter ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            task.addOnSuccessListener {
                emitter.onSuccess(MapperFromGoogleUserToDomainUser.mapFromGoogleUserToDomainUser(it))
            }
            task.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

}