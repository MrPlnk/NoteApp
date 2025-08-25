package ru.krivenchukartem.noteapp.domain.repo


import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Attachment

interface AttachmentRepo {
    suspend fun insertAttachment(attachment: Attachment): Long
    suspend fun deleteAttachment(id: Long)
    fun getAttachmentsByNote(noteId: Long): Flow<List<Attachment>>
}