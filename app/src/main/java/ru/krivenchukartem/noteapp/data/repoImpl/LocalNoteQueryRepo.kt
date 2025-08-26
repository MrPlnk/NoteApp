package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.NoteFullDao
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

class LocalNoteQueryRepo @Inject constructor(
    private val noteFullDao: NoteFullDao
) : NoteQueryRepo{
    override fun getNoteFullById(reqId: Long): Flow<NoteFull?> {
        return noteFullDao.getNoteFullById(reqId).map { it?.toModel() }
    }

    override fun getAllNotesFull(): Flow<List<NoteFull>> {
        return noteFullDao.getAllNotesFull().map { list -> list.map { it.toModel() } }
    }

    override fun filterNotesFull(
        tags: List<String>,
        query: String
    ): Flow<List<NoteFull>> {
        return noteFullDao.filterNotesFull(tags, query)
            .map { list -> list.map { it.toModel() } }
    }
}