package com.bacer.notesapp.data.grades

import androidx.room.Entity

import androidx.room.PrimaryKey
import androidx.room.ForeignKey

import androidx.room.Index
import com.bacer.notesapp.data.subjects.SubjectEntity

@Entity(
    tableName = "grades",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE   // When a subject gets deleted, all the associated grades also get deleted
        )
    ],
    indices = [Index("subjectId")]  // Helps performance
)

data class GradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectId: Int,
    val name: String,
    val value: Double
)