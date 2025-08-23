package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Note

interface NoteRepo {
    suspend fun saveNote(note: Note): Long
    suspend fun updateNote(note: Note): Int
    suspend fun deleteNote(note: Note)
    suspend fun getNoteById(id: Long): Note?
    fun getAllNotes(): Flow<List<Note>>
    fun getNotesByTitle(reqTitle: String): Flow<List<Note>>
}