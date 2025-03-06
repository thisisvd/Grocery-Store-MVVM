package com.example.grocery_store.data.repository

import com.example.grocery_store.data.local.UserDao
import com.example.grocery_store.domain.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    fun getUser(username: String, password: String) = userDao.getUser(username, password)
}