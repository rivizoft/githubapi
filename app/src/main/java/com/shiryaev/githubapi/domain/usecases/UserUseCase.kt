package com.shiryaev.githubapi.domain.usecases

import com.shiryaev.githubapi.domain.User
import com.shiryaev.githubapi.domain.repositories.UserRepository
import toothpick.InjectConstructor

@InjectConstructor
class UserUseCase(private val userRepository: UserRepository)
{
    fun setCurrentUser(user: User?)
    {
        userRepository.currentUser = user
    }

    fun getCurrentUser(): User? = userRepository.currentUser

    fun existLoggedUser(): Boolean
    {
        return userRepository.currentUser != null
    }
}