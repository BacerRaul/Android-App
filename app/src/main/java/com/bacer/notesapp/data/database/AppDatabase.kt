package com.bacer.notesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bacer.notesapp.data.grades.GradeDao
import com.bacer.notesapp.data.grades.GradeEntity
import com.bacer.notesapp.data.subjects.SubjectDao
import com.bacer.notesapp.data.subjects.SubjectEntity
import com.bacer.notesapp.data.notes.NoteDao
import com.bacer.notesapp.data.notes.NoteEntity

@Database(
    entities = [SubjectEntity::class, GradeEntity::class, NoteEntity::class],
    version = 3)
@TypeConverters(com.bacer.notesapp.data.notes.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun gradeDao(): GradeDao
    abstract fun noteDao(): NoteDao
}