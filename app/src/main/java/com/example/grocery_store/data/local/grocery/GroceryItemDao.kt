package com.example.grocery_store.data.local.grocery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocery_store.domain.GroceryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryItemDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertItem(item: GroceryItem): Long

    @Query("SELECT * FROM grocery_item_table")
    fun getGroceryItems(): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_item_table WHERE itemName LIKE '%' || :searchQuery || '%' OR itemPrice LIKE '%' || :searchQuery || '%'")
    fun searchInGroceryItems(searchQuery: String): Flow<List<GroceryItem>>
}