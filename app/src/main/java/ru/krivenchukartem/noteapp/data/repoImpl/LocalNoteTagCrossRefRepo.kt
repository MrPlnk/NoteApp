package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.dao.NoteTagCrossRefDao
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef
import ru.krivenchukartem.noteapp.domain.repo.NoteTagCrossRefRepo
import javax.inject.Inject

/**
 * Репозиторий связей заметок и тегов, работает с [NoteTagCrossRefDao].
 */
class LocalNoteTagCrossRefRepo @Inject constructor(
    private val noteTagCrossRefDao: NoteTagCrossRefDao
) : NoteTagCrossRefRepo {

    /** Привязывает тег к заметке. */
    override suspend fun attachTag(ref: NoteTagCrossRef) {
        noteTagCrossRefDao.attachTag(ref.toEntity())
    }

    /** Отвязывает тег от заметки. */
    override suspend fun detachTag(noteId: Long, tagId: Long) {
        noteTagCrossRefDao.detachTag(noteId, tagId)
    }

    /** Возвращает поток id тегов, связанных с заметкой. */
    override fun tagIdsForNote(noteId: Long): Flow<List<Long>> {
        return noteTagCrossRefDao.tagIdsForNote(noteId)
    }
}