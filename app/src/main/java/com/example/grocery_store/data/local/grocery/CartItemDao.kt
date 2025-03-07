package com.example.grocery_store.data.local.grocery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocery_store.domain.CartCountAndSum
import com.example.grocery_store.domain.GroceryCartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertItemInCart(item: GroceryCartItem): Long

    @Query("SELECT * FROM grocery_cart_table WHERE orderStatus = 0")
    fun getCartItems(): Flow<List<GroceryCartItem>>

    @Query("SELECT * FROM grocery_cart_table WHERE userId=:userId AND itemId=:itemId")
    fun getCartItem(userId: Int, itemId: Int): List<GroceryCartItem>

    @Query("DELETE FROM grocery_cart_table WHERE userId=:userId AND itemId=:itemId")
    suspend fun deleteItemFromCart(userId: Int, itemId: Int): Int

    @Query("UPDATE grocery_cart_table SET orderStatus = 1 WHERE userId=:userId")
    suspend fun updateStatusAllItemFromCart(userId: Int): Int

    @Query("SELECT SUM(itemCount) AS totalCount, SUM(itemPrice * itemCount) AS totalSum FROM grocery_cart_table WHERE userId = :userId AND orderStatus = 0")
    fun getTotalCountAndSum(userId: Int): Flow<CartCountAndSum>
}