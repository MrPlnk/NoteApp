package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.NoteFullDao
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

/**
 * Репозиторий запросов заметок, использует [NoteFullDao] для доступа к данным.
 */
class LocalNoteQueryRepo @Inject constructor(
    private val noteFullDao: NoteFullDao
) : NoteQueryRepo{

    /** Возвращает заметку с вложениями и тегами по id. */
    override fun getNoteFullById(reqId: Long): Flow<NoteFull?> {
        return noteFullDao.getNoteFullById(reqId).map { it?.toModel() }
    }

    /** Возвращает поток всех заметок с вложениями и тегами. */
    override fun getAllNotesFull(): Flow<List<NoteFull>> {
        return noteFullDao.getAllNotesFull().map { list -> list.map { it.toModel() } }
    }

    /** Фильтрует заметки по тегам и поисковому запросу. */
    override fun filterNotesFull(
        tags: List<String>,
        query: String
    ): Flow<List<NoteFull>> {
        return noteFullDao.filterNotesFull(tags, query)
            .map { list -> list.map { it.toModel() } }
    }
}