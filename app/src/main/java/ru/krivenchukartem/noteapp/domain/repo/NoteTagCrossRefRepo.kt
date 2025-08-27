package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef

/**
 * Контракт для работы со связями заметок и тегов (многие-ко-многим).
 */
interface NoteTagCrossRefRepo {
    /** Привязывает тег к заметке. */
    suspend fun attachTag(ref: NoteTagCrossRef)

    /** Отвязывает тег от заметки. */
    suspend fun detachTag(noteId: Long, tagId: Long)

    /** Возвращает flow id тегов для указанной заметки. */
    fun tagIdsForNote(noteId: Long): Flow<List<Long>>
}