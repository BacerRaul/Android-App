package com.bacer.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SubjectEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
}
