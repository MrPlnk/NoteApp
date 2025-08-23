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
    override suspend fun saveNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note): Int {
        return noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
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

    override fun getNotesByTitle(reqTitle: String): Flow<List<Note>> {
        return noteDao.getNotesByTitle(reqTitle)
            .map{ list ->
                list.map{
                    it.toModel()
                }
            }
    }

}