package com.example.grocery_store.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.grocery_store.utils.Constants

@Entity(
    tableName = Constants.GROCERY_ITEM_TABLE,
    indices = [Index(value = ["item_id"], unique = true), Index(
        value = ["itemName"],
        unique = true
    )]
)
class GroceryItem(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "item_id") val itemId: Int = 0,
    val itemName: String,
    val itemDescription: String,
    val itemPrice: Double,
    val itemImage: ByteArray
)