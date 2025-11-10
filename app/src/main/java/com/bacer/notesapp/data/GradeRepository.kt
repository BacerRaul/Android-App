package com.bacer.notesapp.data

import kotlinx.coroutines.flow.Flow

class GradeRepository(private val dao: GradeDao) {

    suspend fun getGrades(subjectId: Int): List<GradeEntity> = dao.getGrades(subjectId)

    suspend fun insertGrade(subjectId: Int, name: String, value: Double) {
        dao.insertGrade(
            GradeEntity(subjectId = subjectId, name = name, value = value)
        )
    }

    suspend fun deleteGrade(grade: GradeEntity) {
        dao.deleteGrade(grade)
    }
}
