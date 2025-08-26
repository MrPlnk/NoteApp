package ru.krivenchukartem.noteapp.domain.repo

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.NoteFullItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem
import ru.krivenchukartem.noteapp.domain.model.NoteFull

interface NoteQueryRepo {
    fun getNoteFullById(reqId: Long): Flow<NoteFull?>
    fun getAllNotesFull(): Flow<List<NoteFull>>
    fun filterNotesFull(tags: List<String>, query: String): Flow<List<NoteFull>>
}