package com.example.grocery_store.data.local.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocery_store.domain.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM user WHERE username=:username AND password=:password")
    fun getUser(username: String, password: String): Flow<User?>
}