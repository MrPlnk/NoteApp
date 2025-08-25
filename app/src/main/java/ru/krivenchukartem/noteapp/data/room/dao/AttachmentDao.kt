package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.Attachment

@Dao
interface AttachmentDao {
    @Insert
    suspend fun insertAttachment(attachment: Attachment): Long

    @Query("""
        DELETE FROM attachments
        WHERE id = :id
    """)
    suspend fun deleteAttachment(id: Long)

    @Query("""
        SELECT * FROM attachments
        WHERE noteId = :noteId ORDER BY id DESC
    """)
    fun attachmentsByNote(noteId: Long): Flow<List<Attachment>>
}