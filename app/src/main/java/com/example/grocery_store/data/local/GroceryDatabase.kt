package com.example.grocery_store.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.grocery_store.data.local.grocery.CartItemDao
import com.example.grocery_store.data.local.grocery.GroceryItemDao
import com.example.grocery_store.data.local.users.UserDao
import com.example.grocery_store.domain.GroceryCartItem
import com.example.grocery_store.domain.GroceryItem
import com.example.grocery_store.domain.User

@Database(
    entities = [User::class, GroceryItem::class, GroceryCartItem::class],
    version = 1,
    exportSchema = false
)
abstract class GroceryDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    abstract val groceryItemDao: GroceryItemDao

    abstract val cartItemDao: CartItemDao
}