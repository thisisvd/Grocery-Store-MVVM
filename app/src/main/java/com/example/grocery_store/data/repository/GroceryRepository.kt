package com.example.grocery_store.data.repository

import com.example.grocery_store.data.local.grocery.CartItemDao
import com.example.grocery_store.data.local.grocery.GroceryItemDao
import com.example.grocery_store.domain.GroceryCartItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceryRepository @Inject constructor(
    private val groceryItemDao: GroceryItemDao, private val cartItemDao: CartItemDao
) {

    fun groceryItems() = groceryItemDao.getGroceryItems()

    fun searchInGroceryItems(item: String) = groceryItemDao.searchInGroceryItems(item)

    suspend fun insertItemInCart(item: GroceryCartItem) = cartItemDao.insertItemInCart(item)

    fun getCartItems() = cartItemDao.getCartItems()

    fun getCartItem(userId: Int, itemId: Int) = cartItemDao.getCartItem(userId, itemId)

    suspend fun deleteItemFromCart(userId: Int, itemId: Int) =
        cartItemDao.deleteItemFromCart(userId, itemId)

    suspend fun updateStatusAllItemFromCart(userId: Int) =
        cartItemDao.updateStatusAllItemFromCart(userId)

    fun getTotalCountAndSum(userId: Int) = cartItemDao.getTotalCountAndSum(userId)
}