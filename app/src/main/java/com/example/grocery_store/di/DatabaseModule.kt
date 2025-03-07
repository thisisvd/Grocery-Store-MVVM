package com.example.grocery_store.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.grocery_store.data.local.GroceryDatabase
import com.example.grocery_store.utils.Constants.DATABASE_NAME
import com.example.grocery_store.utils.Constants.SHARED_PREFERENCE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, GroceryDatabase::class.java, DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: GroceryDatabase) = db.userDao

    @Singleton
    @Provides
    fun provideGroceryItemDao(db: GroceryDatabase) = db.groceryItemDao

    @Singleton
    @Provides
    fun provideCartItemDao(db: GroceryDatabase) = db.cartItemDao

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }
}