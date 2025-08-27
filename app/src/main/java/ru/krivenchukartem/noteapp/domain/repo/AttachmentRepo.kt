package ru.krivenchukartem.noteapp.domain.repo


import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Attachment

/**
 * Контракт доступа к вложениям (Domain слой).
 */
interface AttachmentRepo {
    /** Добавляет вложение и возвращает его id. */
    suspend fun insertAttachment(attachment: Attachment): Long

    /** Удаляет вложение по id. */
    suspend fun deleteAttachment(id: Long)

    /** Возвращает flow вложений для указанной заметки. */
    fun getAttachmentsByNote(noteId: Long): Flow<List<Attachment>>
}