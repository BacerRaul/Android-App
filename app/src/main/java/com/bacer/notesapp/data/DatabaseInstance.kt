package com.bacer.notesapp.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "subjects_database"
            )
                .fallbackToDestructiveMigration(true)
                .build().also {
                INSTANCE = it
            }
        }
    }
}
