package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.NoteDao
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

class LocalNoteRepo @Inject constructor(
    private val noteDao: NoteDao
): NoteRepo {
    override suspend fun saveNote(title: String, body: String): Long {
        val note = Note(
            title = title,
            body = body,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(id: Long, title: String, body: String): Int {
        val updatedAt = System.currentTimeMillis()
        return noteDao.updateNote(id, title, body, updatedAt)
    }

    override suspend fun deleteNote(id: Long) {
        noteDao.deleteNote(id)
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.toModel()
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
            .map { list ->
                list.map {
                    it.toModel()
                }
            }
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return noteDao.search(query)
            .map{ list ->
                list.map{
                    it.toModel()
                }
            }
    }

}