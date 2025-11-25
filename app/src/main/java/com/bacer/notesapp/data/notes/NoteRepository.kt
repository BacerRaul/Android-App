package com.bacer.notesapp.data.notes

class NoteRepository(private val dao: NoteDao) {

    suspend fun getNotes(subjectId: Int): List<NoteEntity> =
        dao.getNotes(subjectId)

    suspend fun insertNote(subjectId: Int, name: String, imageUris: List<String>) {
        dao.insertNote(
            NoteEntity(
                subjectId = subjectId,
                name = name,
                imageUris = imageUris
            )
        )
    }

    suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }
}
