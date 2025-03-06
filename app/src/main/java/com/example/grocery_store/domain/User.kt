package com.example.grocery_store.domain

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.grocery_store.utils.Constants.USER_TABLE

@Entity(tableName = USER_TABLE, indices = [Index(value = ["username"], unique = true)])
class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0, val username: String, val password: String
)