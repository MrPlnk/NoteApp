package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Note

interface NoteRepo {
    suspend fun saveNote(title: String, body: String): Long
    suspend fun updateNote(id: Long, title: String, body: String): Int
    suspend fun deleteNote(id: Long)
    suspend fun getNoteById(id: Long): Note?
    fun getAllNotes(): Flow<List<Note>>
    fun searchNote(query: String): Flow<List<Note>>
}