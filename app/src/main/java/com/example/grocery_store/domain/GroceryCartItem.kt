package com.example.grocery_store.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocery_store.utils.Constants

@Entity(
    tableName = Constants.GROCERY_CART_TABLE,
)
class GroceryCartItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "cart_item_id") val cartItemId: Int = 0,
    val itemId: Int,
    val itemName: String,
    val itemDescription: String,
    val itemPrice: Double,
    val itemImage: ByteArray,
    val itemCount: Int = 1,
    val userId: Int,
    val orderStatus: Boolean = false
)