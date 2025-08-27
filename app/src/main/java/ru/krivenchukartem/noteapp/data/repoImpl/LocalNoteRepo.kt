package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.NoteDao
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

/**
 * Репозиторий заметок, работает с локальной БД через [NoteDao].
 */
class LocalNoteRepo @Inject constructor(
    private val noteDao: NoteDao
): NoteRepo {

    /** Создаёт новую заметку и возвращает её id. */
    override suspend fun saveNote(title: String, body: String, isPinned: Boolean): Long {
        val note = Note(
            title = title,
            body = body,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isPinned = isPinned,
        )
        return noteDao.insertNote(note.toEntity())
    }

    /** Обновляет существующую заметку и возвращает количество изменённых строк. */
    override suspend fun updateNote(id: Long, title: String, body: String, isPinned: Boolean): Int {
        val updatedAt = System.currentTimeMillis()
        return noteDao.updateNote(
            id = id,
            title = title,
            body = body,
            isPinned = isPinned,
            updatedAt = updatedAt
        )
    }

    /** Удаляет заметку по id. */
    override suspend fun deleteNote(id: Long) {
        noteDao.deleteNote(id)
    }

    /** Возвращает заметку по id. */
    override fun getNoteById(id: Long): Flow<Note?> {
        return noteDao.getNoteById(id)
            .map{ it?.toModel() }
    }

    /** Возвращает flow всех заметок. */
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
            .map { list ->
                list.map {
                    it.toModel()
                }
            }
    }
}