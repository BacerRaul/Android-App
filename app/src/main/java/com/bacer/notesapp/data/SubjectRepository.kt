package com.bacer.notesapp.data

import kotlinx.coroutines.flow.Flow

class SubjectRepository(private val dao: SubjectDao) {

    suspend fun getSubjects(): List<SubjectEntity> = dao.getAllSubjects()

    suspend fun insertSubject(name: String) {
        dao.insertSubject(SubjectEntity(name = name))
    }

    suspend fun deleteSubject(subject: SubjectEntity) {
        dao.deleteSubject(subject)
    }

    fun getSubjectById(id: Int): Flow<SubjectEntity?> {
        return dao.getSubjectById(id)
    }

}
