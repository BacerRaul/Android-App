package com.bacer.notesapp.data.grades

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {

    @Query("SELECT * FROM grades WHERE subjectId = :subjectId")
    suspend fun getGrades(subjectId: Int): List<GradeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity)

    @Delete
    suspend fun deleteGrade(grade: GradeEntity)

    @Query("SELECT * FROM grades WHERE subjectId = :subjectId")
    fun getGradesFlow(subjectId: Int): Flow<List<GradeEntity>>
}
