package com.bacer.notesapp.data

class SubjectRepository(private val dao: SubjectDao) {

    suspend fun getSubjects(): List<SubjectEntity> = dao.getAllSubjects()

    suspend fun insertSubject(name: String) {
        dao.insertSubject(SubjectEntity(name = name))
    }

    suspend fun deleteSubject(subject: SubjectEntity) {
        dao.deleteSubject(subject)
    }
}
