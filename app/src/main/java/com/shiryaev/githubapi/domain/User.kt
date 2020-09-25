package com.shiryaev.githubapi.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    var uid: String?,
    val name: String,
    var accessToken: AccessToken?,
    var favoriteRepositories: List<RepositoryData>?
)
