package com.hasanali.kotlincountries.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hasanali.kotlincountries.model.Country
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Country::class], version = 1)
abstract class CountryDatabase: RoomDatabase() {

    abstract fun getDao(): CountryDAO

    companion object {
        @Volatile private var instance: CountryDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        operator fun invoke(context: Context) = instance ?: synchronized(Any()) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,CountryDatabase::class.java,"countrydatabase").build()
    }

}