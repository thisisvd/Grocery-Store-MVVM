package com.example.grocery_store.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.grocery_store.utils.Constants.SP_USER_ID
import com.example.grocery_store.utils.Constants.SP_USER_ID_INVALID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLoginState(userId: Int) {
        sharedPreferences.edit {
            putInt(SP_USER_ID, userId)
        }
    }

    fun getLoginState(): Int {
        return sharedPreferences.getInt(SP_USER_ID, SP_USER_ID_INVALID)
    }
}