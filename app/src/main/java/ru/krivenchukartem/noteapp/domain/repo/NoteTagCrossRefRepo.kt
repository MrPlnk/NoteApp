package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef

interface NoteTagCrossRefRepo {
    suspend fun attachTag(ref: NoteTagCrossRef)
    suspend fun detachTag(noteId: Long, tagId: Long)
    fun tagIdsForNote(noteId: Long): Flow<List<Long>>
}