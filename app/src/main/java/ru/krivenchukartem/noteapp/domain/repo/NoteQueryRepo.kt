package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteFull

/**
 * Контракт запросов заметок с тегами и вложениями (Domain слой).
 */
interface NoteQueryRepo {
    /** Возвращает заметку с тегами и вложениями по id. */
    fun getNoteFullById(reqId: Long): Flow<NoteFull?>

    /** Возвращает поток всех заметок с тегами и вложениями. */
    fun getAllNotesFull(): Flow<List<NoteFull>>

    /** Фильтрует заметки по тегам и строке поиска. */
    fun filterNotesFull(tags: List<String>, query: String): Flow<List<NoteFull>>
}