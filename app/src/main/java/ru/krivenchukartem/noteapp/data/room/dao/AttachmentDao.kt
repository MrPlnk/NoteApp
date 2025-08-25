package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.AttachmentItem

@Dao
interface AttachmentDao {
    @Insert
    suspend fun insertAttachment(attachmentItem: AttachmentItem): Long

    @Query("""
        DELETE FROM attachments
        WHERE id = :id
    """)
    suspend fun deleteAttachment(id: Long)

    @Query("""
        SELECT * FROM attachments
        WHERE noteId = :noteId ORDER BY id DESC
    """)
    fun getAttachmentsByNote(noteId: Long): Flow<List<AttachmentItem>>
}