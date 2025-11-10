package com.bacer.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SubjectEntity::class, GradeEntity::class],
    version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun gradeDao(): GradeDao
}
