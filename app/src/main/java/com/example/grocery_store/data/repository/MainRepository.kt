package com.example.grocery_store.data.repository

import android.content.Context
import com.example.grocery_store.R
import com.example.grocery_store.core.Utils
import com.example.grocery_store.data.local.grocery.GroceryItemDao
import com.example.grocery_store.data.local.users.UserDao
import com.example.grocery_store.domain.GroceryItem
import com.example.grocery_store.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val context: Context,
    private val userDao: UserDao,
    private val groceryItemDao: GroceryItemDao
) {

    val groceryItems = listOf(
        GroceryItem(
            1,
            "Apple Grape Juice",
            "2L",
            199.0,
            Utils.drawableToByteArray(context, R.drawable.img_apple_grape_juice)
        ),
        GroceryItem(
            2,
            "Bell Pepper",
            "1kg",
            120.0,
            Utils.drawableToByteArray(context, R.drawable.img_bell_pepper_red)
        ),
        GroceryItem(
            3,
            "Coca Cola Can",
            "325ml",
            45.0,
            Utils.drawableToByteArray(context, R.drawable.img_coca_cola_can)
        ),
        GroceryItem(
            4,
            "Diet Coke",
            "335ml",
            50.0,
            Utils.drawableToByteArray(context, R.drawable.img_diet_coke)
        ),
        GroceryItem(
            5,
            "Ginger",
            "250gm",
            60.0,
            Utils.drawableToByteArray(context, R.drawable.img_ginger)
        ),
        GroceryItem(
            6,
            "Mayonnaise Eggless",
            "500gm",
            175.0,
            Utils.drawableToByteArray(context, R.drawable.img_mayonnais_eggless)
        ),
        GroceryItem(
            7,
            "Natural Red Apple",
            "1kg",
            220.0,
            Utils.drawableToByteArray(context, R.drawable.img_natural_red_apple)
        ),
        GroceryItem(
            8,
            "Orange Juice",
            "2L",
            210.0,
            Utils.drawableToByteArray(context, R.drawable.img_orange_juice)
        ),
        GroceryItem(
            9,
            "Organic Bananas",
            "7pcs",
            90.0,
            Utils.drawableToByteArray(context, R.drawable.img_orange_juice)
        ),
        GroceryItem(
            10,
            "Pepsi Can",
            "330ml",
            45.0,
            Utils.drawableToByteArray(context, R.drawable.img_pepsi_can)
        ),
        GroceryItem(
            11,
            "Sprite Can",
            "325ml",
            45.0,
            Utils.drawableToByteArray(context, R.drawable.img_sprite_can)
        ),
        GroceryItem(
            12,
            "Broccoli",
            "500gm",
            50.0,
            Utils.drawableToByteArray(context, R.drawable.img_broccoli)
        )
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            groceryItems.forEach {
                groceryItemDao.insertItem(it)
            }
        }
    }

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    fun getUser(username: String, password: String) = userDao.getUser(username, password)
}