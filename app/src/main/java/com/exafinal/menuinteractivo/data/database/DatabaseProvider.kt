package com.exafinal.menuinteractivo.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: RestauranteDatabase? = null

    fun getDatabase(context: Context): RestauranteDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                RestauranteDatabase::class.java,
                "restaurante_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}