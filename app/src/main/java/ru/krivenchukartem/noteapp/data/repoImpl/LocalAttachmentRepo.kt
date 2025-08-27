package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.AttachmentDao
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.Attachment
import ru.krivenchukartem.noteapp.domain.repo.AttachmentRepo
import javax.inject.Inject
/**
 * Репозиторий для вложений, работает с локальной БД через [AttachmentDao].
 */
class LocalAttachmentRepo @Inject constructor(
    private val attachmentDao: AttachmentDao
) : AttachmentRepo{

    /** Добавляет вложение и возвращает его id. */
    override suspend fun insertAttachment(attachment: Attachment): Long {
        return attachmentDao.insertAttachment(attachment.toEntity())
    }

    /** Удаляет вложение по id. */
    override suspend fun deleteAttachment(id: Long) {
        attachmentDao.deleteAttachment(id)
    }

    /** Возвращает flow вложений для указанной заметки. */
    override fun getAttachmentsByNote(noteId: Long): Flow<List<Attachment>> {
        return attachmentDao.getAttachmentsByNote(noteId).map { list -> list.map { it.toModel() } }
    }
}