package com.shiryaev.githubapi.domain.repositories

import com.shiryaev.githubapi.domain.User

interface UserRepository
{
    var currentUser: User?
    fun addUser(user: User)
}