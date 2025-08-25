package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.dao.NoteTagCrossRefDao
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef
import ru.krivenchukartem.noteapp.domain.repo.NoteTagCrossRefRepo

class LocalNoteTagCrossRefRepo(
    private val noteTagCrossRefDao: NoteTagCrossRefDao
) : NoteTagCrossRefRepo {
    override suspend fun attachTag(ref: NoteTagCrossRef) {
        noteTagCrossRefDao.attachTag(ref.toEntity())
    }

    override suspend fun detachTag(noteId: Long, tagId: Long) {
        noteTagCrossRefDao.detachTag(noteId, tagId)
    }

    override fun tagIdsForNote(noteId: Long): Flow<List<Long>> {
        return noteTagCrossRefDao.tagIdsForNote(noteId)
    }
}