package com.shiryaev.githubapi.data.repositoriesImpl

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.shiryaev.githubapi.domain.User
import com.shiryaev.githubapi.domain.repositories.UserRepository
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class UserRepositoryImpl(private val activity: Activity) : UserRepository
{
    private val CURRENT_USER_KEY = "CURRENT_USER"

    override var currentUser: User? = loadCurrentUserFromPreferences()
        set(value)
        {
            if (value != null && value.uid == null)
            {
                value.uid = generateUid().toString()
                addCurrentUserToPreferences(value)
            }

            if (value == null && field != null)
                deleteCurrentUserFromPreferences()

            field = value
        }
        get()
        {
            return loadCurrentUserFromPreferences()
        }

    override fun addUser(user: User)
    {

    }

    private fun deleteCurrentUserFromPreferences()
    {
        val prefs = activity.getPreferences(MODE_PRIVATE)
        if (prefs.contains(CURRENT_USER_KEY))
            prefs.edit().apply {
                remove(CURRENT_USER_KEY)
                commit()
            }
    }

    private fun loadCurrentUserFromPreferences(): User?
    {
        val prefs = activity.getPreferences(MODE_PRIVATE)
        val json = prefs.getString(CURRENT_USER_KEY, null) ?: return null
        return Gson().fromJson(json, User::class.java)
    }

    private fun addCurrentUserToPreferences(user: User)
    {
        val prefs = activity.getPreferences(MODE_PRIVATE)
        val editor = prefs.edit()
        
        val jsonObject = Gson().toJson(user)

        editor.putString(CURRENT_USER_KEY, jsonObject)
        editor.commit()
    }

    private fun generateUid(): UUID
    {
        return UUID.randomUUID()
    }
}