package com.example.grocery_store.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grocery_store.domain.User

@Database(
    entities = [User::class], version = 1, exportSchema = false
)
abstract class GroceryDatabase : RoomDatabase() {

    abstract val userDao: UserDao
}