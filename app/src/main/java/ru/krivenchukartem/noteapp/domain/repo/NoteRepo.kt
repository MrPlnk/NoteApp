package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Note

/**
 * Репозиторий заметок (контракт Domain слоя).
 */
interface NoteRepo {
    /** Создаёт заметку и возвращает её id. */
    suspend fun saveNote(title: String, body: String, isPinned: Boolean): Long

    /** Обновляет заметку и возвращает количество изменённых строк. */
    suspend fun updateNote(id: Long, title: String, body: String, isPinned: Boolean): Int

    /** Удаляет заметку по id. */
    suspend fun deleteNote(id: Long)

    /** Возвращает заметку по id или null, если не найдена. */
    fun getNoteById(id: Long): Flow<Note?>

    /** Возвращает поток всех заметок. */
    fun getAllNotes(): Flow<List<Note>>
}